package org.onlinebankingweb.dto.responses.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;

public class BankTransferResponse extends PaymentInstrumentResponse {
    private final String description;

    public BankTransferResponse(BankTransfer bankTransfer) {
        super(bankTransfer);
        this.description = "Bank Transfer";
    }

    @Override
    public String toString() {
        return description;
    }
}
