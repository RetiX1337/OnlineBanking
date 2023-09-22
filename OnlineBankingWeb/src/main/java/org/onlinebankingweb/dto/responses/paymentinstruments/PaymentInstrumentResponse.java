package org.onlinebankingweb.dto.responses.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebankingweb.dto.responses.BankAccountResponse;

public abstract class PaymentInstrumentResponse {
    private final Long id;
    private final BankAccountResponse bankAccountResponse;

    public PaymentInstrumentResponse(PaymentInstrument paymentInstrument) {
        this.id = paymentInstrument.getId();
        this.bankAccountResponse = new BankAccountResponse(paymentInstrument.getBankAccount());
    }

    public Long getId() {
        return id;
    }

    public BankAccountResponse getBankAccountResponse() {
        return bankAccountResponse;
    }

}
