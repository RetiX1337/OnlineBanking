package org.onlinebankingweb.dto.requests;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.transactions.TransactionStatus;
import org.onlinebanking.core.domain.models.transactions.TransactionType;

import java.math.BigDecimal;

public class TransactionRequest {
    private TransactionType transactionType;
    private Long paymentInstrumentId;
    private String senderBankAccountNumber;
    private String receiverBankAccountNumber;
    private BigDecimal amount;

    public TransactionRequest() {

    }

    public void setPaymentInstrument(Long paymentInstrumentId) {
        this.paymentInstrumentId = paymentInstrumentId;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setReceiverBankAccountNumber(String receiverBankAccountNumber) {
        this.receiverBankAccountNumber = receiverBankAccountNumber;
    }

    public void setSenderBankAccountNumber(String senderBankAccountNumber) {
        this.senderBankAccountNumber = senderBankAccountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPaymentInstrumentId() {
        return paymentInstrumentId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getReceiverBankAccountNumber() {
        return receiverBankAccountNumber;
    }

    public String getSenderBankAccountNumber() {
        return senderBankAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
