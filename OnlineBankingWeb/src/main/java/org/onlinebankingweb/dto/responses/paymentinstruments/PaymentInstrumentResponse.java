package org.onlinebankingweb.dto.responses.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebankingweb.dto.responses.BankAccountResponse;

public abstract class PaymentInstrumentResponse {
    private final String id;
    private final BankAccountResponse bankAccountResponse;

    public PaymentInstrumentResponse(PaymentInstrument paymentInstrument) {
        this.id = String.valueOf(paymentInstrument.getId());
        this.bankAccountResponse = new BankAccountResponse(paymentInstrument.getBankAccount());
    }

    public String getId() {
        return id;
    }

    public BankAccountResponse getBankAccountResponse() {
        return bankAccountResponse;
    }
}
