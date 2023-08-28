package org.onlinebanking.core.domain.dto;

import org.onlinebanking.core.domain.models.BankAccount;

public abstract class PaymentInstrumentDTO {
    protected BankAccount bankAccount;

    public PaymentInstrumentDTO() {

    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }
}
