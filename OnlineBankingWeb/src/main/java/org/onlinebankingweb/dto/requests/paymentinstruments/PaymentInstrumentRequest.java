package org.onlinebankingweb.dto.requests.paymentinstruments;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.DebitCardRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "paymentInstrumentRequestType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BankTransferRequest.class, name = "BANK_TRANSFER"),
        @JsonSubTypes.Type(value = CreditCardRequest.class, name = "CREDIT_CARD"),
        @JsonSubTypes.Type(value = DebitCardRequest.class, name = "DEBIT_CAD")
})
public abstract class PaymentInstrumentRequest {
    private String bankAccountNumber;

    public PaymentInstrumentRequest() {

    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }
}
