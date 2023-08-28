package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.dto.TransactionDTO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

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


    @Override
    public boolean processPayment(TransactionDTO transactionDTO) {
        BankAccount sender = transactionDTO.getSender();
        BankAccount receiver = transactionDTO.getReceiver();
        BigDecimal amount = transactionDTO.getAmount();
        PaymentInstrument paymentInstrument = transactionDTO.getPaymentInstrument();

        if (!(sender.isActive() && receiver.isActive())) {
            return false;
        }

        if (paymentInstrumentService.processPayment(transactionDTO)) {
            bankAccountDAO.update(sender);
            bankAccountDAO.update(receiver);
            return true;
        }

        return false;
    }

    @Override
    public List<Transaction> findByBankAccount(BankAccount bankAccount) {
        return null;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return null;
    }
}
