package org.onlinebankingweb.dto.responses;

import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.onlinebanking.core.domain.models.transactions.TransactionStatus;
import org.onlinebanking.core.domain.models.transactions.TransactionType;
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponse;
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponseFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionResponse {
    private final Long id;
    private final Timestamp transactionDate;
    private final String transactionName;
    private final TransactionType transactionType;
    private final TransactionStatus transactionStatus;
    private final BankAccountResponse sender;
    private final BankAccountResponse receiver;
    private final PaymentInstrumentResponse paymentInstrumentResponse;
    private final BigDecimal amount;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.transactionDate = transaction.getTransactionDate();
        this.transactionName = transaction.getTransactionName();
        this.transactionType = transaction.getTransactionType();
        this.transactionStatus = transaction.getTransactionStatus();
        this.sender = new BankAccountResponse(transaction.getSender());
        this.receiver = new BankAccountResponse(transaction.getReceiver());
        this.paymentInstrumentResponse = PaymentInstrumentResponseFactory
                .createPaymentInstrument(transaction.getPaymentInstrument());
        this.amount = transaction.getAmount();
    }

    public Long getId() {
        return id;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BankAccountResponse getReceiver() {
        return receiver;
    }

    public BankAccountResponse getSender() {
        return sender;
    }

    public PaymentInstrumentResponse getPaymentInstrumentResponse() {
        return paymentInstrumentResponse;
    }
}
