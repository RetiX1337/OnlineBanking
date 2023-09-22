package org.onlinebanking.core.domain.dto.requests.paymentinstruments;


public abstract class PaymentInstrumentRequest {
    protected String bankAccountNumber;

    public PaymentInstrumentRequest() {

    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }
}
