package org.onlinebankingweb.dto.requests.paymentinstruments;


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
