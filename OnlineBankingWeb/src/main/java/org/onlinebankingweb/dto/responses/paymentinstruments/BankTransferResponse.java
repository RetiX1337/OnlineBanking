package org.onlinebankingweb.dto.responses.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;

public class BankTransferResponse extends PaymentInstrumentResponse {
    public BankTransferResponse(BankTransfer bankTransfer) {
        super(bankTransfer);
    }

    @Override
    public String toString() {
        return "Bank Transfer";
    }
}
