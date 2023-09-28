package org.onlinebanking.core.domain.servicedto;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.transactions.TransactionStatus;
import org.onlinebanking.core.domain.models.transactions.TransactionType;

import java.math.BigDecimal;

public class TransactionServiceDTO {
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private PaymentInstrument paymentInstrument;
    private BankAccount sender;
    private BankAccount receiver;
    private BigDecimal amount;

    public TransactionServiceDTO() {

    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setSender(BankAccount sender) {
        this.sender = sender;
    }

    public void setReceiver(BankAccount receiver) {
        this.receiver = receiver;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentInstrument getPaymentInstrument() {
        return paymentInstrument;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public BankAccount getSender() {
        return sender;
    }

    public BankAccount getReceiver() {
        return receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
