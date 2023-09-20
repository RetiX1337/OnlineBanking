package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.dto.requests.TransactionRequest;
import org.onlinebanking.core.domain.exceptions.FailedTransactionException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.onlinebanking.core.domain.models.transactions.TransactionStatus;
import org.onlinebanking.core.domain.models.transactions.TransactionType;
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
    public Transaction processPayment(TransactionRequest transactionRequest) {
        BankAccount sender = transactionRequest.getSender();
        BankAccount receiver = transactionRequest.getReceiver();
        BigDecimal amount = transactionRequest.getAmount();
        PaymentInstrument paymentInstrument = transactionRequest.getPaymentInstrument();

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

            Transaction transaction = createTransaction(transactionRequest);

            return transactionDAO.save(transaction);
        } else {
            throw new FailedTransactionException(
                    String.format(FAILED_TRANSACTION_EXCEPTION_MESSAGE, sender.getAccountNumber()));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> findByBankAccount(BankAccount bankAccount) {
        return transactionDAO.findBySenderBankAccount(bankAccount);
    }

    @Transactional
    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionDAO.update(transaction);
    }

    private Transaction createTransaction(TransactionRequest transactionRequest) {
        BankAccount sender = transactionRequest.getSender();
        BankAccount receiver = transactionRequest.getReceiver();

        Transaction transaction = new Transaction();
        transaction.setPaymentInstrument(transactionRequest.getPaymentInstrument());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setTransactionName(createTransactionName(sender, receiver));
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.TRANSFER);

        return transaction;
    }

    private String createTransactionName(BankAccount sender, BankAccount receiver) {
        return sender.getAccountHolder().getFirstName() + " to " + receiver.getAccountHolder().getFirstName() + " " + receiver.getAccountHolder().getAddress();
    }
}
