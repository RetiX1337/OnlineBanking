package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.dto.TransactionDTO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.onlinebanking.core.domain.models.transactions.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

public class TransactionServiceImpl implements TransactionService {
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
    public boolean processPayment(TransactionDTO transactionDTO) {
        BankAccount sender = transactionDTO.getSender();
        BankAccount receiver = transactionDTO.getReceiver();
        BigDecimal amount = transactionDTO.getAmount();
        PaymentInstrument paymentInstrument = transactionDTO.getPaymentInstrument();

        if (!(sender.isActive() && receiver.isActive())) {
            return false;
        }

        if (paymentInstrument.withdraw(amount)) {
            receiver.deposit(amount);

            bankAccountService.updateBankAccount(sender);
            bankAccountService.updateBankAccount(receiver);

            paymentInstrumentService.updatePaymentInstrument(paymentInstrument);

            Transaction transaction = createTransaction(transactionDTO);

            transactionDAO.save(transaction);

            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> findByBankAccount(BankAccount bankAccount) {
        return null;
    }

    @Transactional
    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return null;
    }

    private Transaction createTransaction(TransactionDTO transactionDTO) {
        BankAccount sender = transactionDTO.getSender();
        BankAccount receiver = transactionDTO.getReceiver();

        Transaction transaction = new Transaction();
        transaction.setPaymentInstrument(transactionDTO.getPaymentInstrument());
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setTransactionName(createTransactionName(sender, receiver));
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(transactionDTO.getTransactionType());

        return transaction;
    }

    private String createTransactionName(BankAccount sender, BankAccount receiver) {
        return sender.getAccountHolder().getFirstName() + " to " + receiver.getAccountHolder().getFirstName() + " " + receiver.getAccountHolder().getAddress();
    }
}
