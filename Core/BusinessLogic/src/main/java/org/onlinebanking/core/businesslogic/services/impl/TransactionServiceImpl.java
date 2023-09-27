package org.onlinebanking.core.businesslogic.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.servicedto.TransactionServiceDTO;
import org.onlinebanking.core.domain.exceptions.DAOException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.FailedTransactionException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.onlinebanking.core.domain.models.transactions.TransactionStatus;
import org.onlinebanking.core.domain.models.transactions.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final String FAILED_TRANSACTION_EXCEPTION_MESSAGE_INACTIVE_ACCOUNT
            = "Transaction couldn't be processed for sender %s, receiver %s";
    private static final String FAILED_TRANSACTION_EXCEPTION_MESSAGE
            = "Transaction couldn't be processed for sender %s";
    private static final String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "Transactions couldn't be found for %s";
    private final static Logger logger = LogManager.getLogger(TransactionServiceImpl.class);
    private final TransactionDAO transactionDAO;
    private final BankAccountService bankAccountService;
    private final PaymentInstrumentService paymentInstrumentService;

    public TransactionServiceImpl(@Autowired TransactionDAO transactionDAO,
                                  @Autowired BankAccountService bankAccountService,
                                  @Autowired PaymentInstrumentService paymentInstrumentService) {
        this.transactionDAO = transactionDAO;
        this.bankAccountService = bankAccountService;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    @Transactional
    @Override
    public Transaction processPayment(TransactionServiceDTO transactionServiceDTO) {
        BankAccount sender = transactionServiceDTO.getSender();
        BankAccount receiver = transactionServiceDTO.getReceiver();
        BigDecimal amount = transactionServiceDTO.getAmount();
        PaymentInstrument paymentInstrument = transactionServiceDTO.getPaymentInstrument();

        if (!sender.isActive() || !receiver.isActive()) {
            throw new FailedTransactionException(
                    String.format(FAILED_TRANSACTION_EXCEPTION_MESSAGE_INACTIVE_ACCOUNT, sender.getAccountNumber(),
                            receiver.getAccountNumber()));
        }

        if (paymentInstrument.withdraw(amount)) {
            receiver.deposit(amount);
            bankAccountService.updateBankAccount(sender);
            bankAccountService.updateBankAccount(receiver);
            paymentInstrumentService.updatePaymentInstrument(paymentInstrument);
            Transaction transaction = createTransaction(transactionServiceDTO);
            try {
                return transactionDAO.save(transaction);
            } catch (PersistenceException e) {
                logger.error(e);
                throw new DAOException();
            }
        } else {
            throw new FailedTransactionException(
                    String.format(FAILED_TRANSACTION_EXCEPTION_MESSAGE, sender.getAccountNumber()));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> findByBankAccount(BankAccount bankAccount) {
        List<Transaction> paymentInstruments;
        try {
            paymentInstruments = transactionDAO.findBySenderBankAccount(bankAccount);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new DAOException();
        }
        if (paymentInstruments.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                    "Bank Account " + bankAccount.getAccountNumber()));
        }
        return paymentInstruments;
    }

    @Transactional
    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionDAO.update(transaction);
    }

    private Transaction createTransaction(TransactionServiceDTO transactionServiceDTO) {
        BankAccount sender = transactionServiceDTO.getSender();
        BankAccount receiver = transactionServiceDTO.getReceiver();

        Transaction transaction = new Transaction();
        transaction.setPaymentInstrument(transactionServiceDTO.getPaymentInstrument());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setTransactionName(createTransactionName(sender, receiver));
        transaction.setAmount(transactionServiceDTO.getAmount());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.TRANSFER);

        return transaction;
    }

    private String createTransactionName(BankAccount sender, BankAccount receiver) {
        return sender.getAccountHolder().getFirstName() + " to " + receiver.getAccountHolder().getFirstName() + " " + receiver.getAccountHolder().getAddress();
    }
}
