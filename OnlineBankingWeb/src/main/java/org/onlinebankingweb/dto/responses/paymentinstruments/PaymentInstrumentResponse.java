package org.onlinebankingweb.dto.responses.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;

public abstract class PaymentInstrumentResponse {
    private final String id;
    private final String bankAccountNumber;

    public PaymentInstrumentResponse(PaymentInstrument paymentInstrument) {
        this.id = String.valueOf(paymentInstrument.getId());
        this.bankAccountNumber = paymentInstrument.getBankAccount().getAccountNumber();
    }

    public String getId() {
        return id;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }
}
