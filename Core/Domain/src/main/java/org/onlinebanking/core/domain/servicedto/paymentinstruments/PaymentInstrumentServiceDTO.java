package org.onlinebanking.core.domain.servicedto.paymentinstruments;


import org.onlinebanking.core.domain.models.BankAccount;

public abstract class PaymentInstrumentServiceDTO {
    protected BankAccount bankAccount;

    public PaymentInstrumentServiceDTO() {

    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }
}
