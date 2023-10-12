package org.onlinebanking.core.businesslogic.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.exceptions.ServiceException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.FailedTransactionException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.onlinebanking.core.domain.models.transactions.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final String FAILED_TRANSACTION_EXCEPTION_MESSAGE_INACTIVE_ACCOUNT
            = "Transaction couldn't be processed for sender %s, receiver %s";
    private static final String FAILED_TRANSACTION_EXCEPTION_MESSAGE
            = "Transaction couldn't be processed for sender %s";
    private static final String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "Transaction couldn't be found for %s";
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
    public Transaction processPayment(Transaction transaction) {
        if (isInvalid(transaction)) {
            throw new ServiceException();
        }

        BankAccount sender = transaction.getSender();
        BankAccount receiver = transaction.getReceiver();
        BigDecimal amount = transaction.getAmount();
        PaymentInstrument paymentInstrument = transaction.getPaymentInstrument();

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
            try {
                populateTransaction(transaction);
                return transactionDAO.save(transaction);
            } catch (Exception e) {
                logger.error(e);
                throw new ServiceException();
            }
        } else {
            throw new FailedTransactionException(String.format(FAILED_TRANSACTION_EXCEPTION_MESSAGE, sender.getAccountNumber()));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> findByBankAccount(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new ServiceException();
        }

        try {
            return transactionDAO.findBySenderBankAccount(bankAccount);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Transaction findById(Long id) {
        if (id == null) {
            throw new ServiceException();
        }

        Transaction transaction;
        try {
            transaction = transactionDAO.findById(id);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }

        if (transaction == null) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, "ID " + id));
        }
        return transaction;
    }

    private void populateTransaction(Transaction transaction) {
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setTransactionName(createTransactionName(transaction.getSender(), transaction.getReceiver()));
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
    }

    private String createTransactionName(BankAccount sender, BankAccount receiver) {
        return sender.getAccountHolder().getFirstName() + " to " + receiver.getAccountHolder().getFirstName() + " " + receiver.getAccountHolder().getAddress();
    }

    private static boolean isInvalid(Transaction transaction) {
        return transaction == null || transaction.getSender() == null || transaction.getReceiver() == null || transaction.getAmount() == null || transaction.getPaymentInstrument() == null;
    }
}
