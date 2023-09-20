package org.onlinebanking.core.domain.dto.requests;

import org.onlinebanking.core.domain.models.BankAccount;

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
