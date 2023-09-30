package org.onlinebankingweb.dto.requests;

import org.onlinebanking.core.domain.models.transactions.TransactionType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransactionRequest {
    @NotNull(message = "Transaction Type can't be null")
    private TransactionType transactionType;
    private String paymentInstrumentId;
    private String senderBankAccountNumber;
    @NotBlank(message = "Receiver Bank account can't be blank")
    private String receiverBankAccountNumber;
    @NotNull(message = "Amount can't be null")
    @DecimalMin(value = "0", inclusive = false, message = "Amount has to be higher than 0")
    private BigDecimal amount;

    public TransactionRequest() {

    }

    public void setPaymentInstrumentId(String paymentInstrumentId) {
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

    public String getPaymentInstrumentId() {
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
