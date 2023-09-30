package org.onlinebankingweb.dto.requests.paymentinstruments.cards;

import org.onlinebankingweb.dto.requests.paymentinstruments.PaymentInstrumentRequest;

public abstract class CardRequest extends PaymentInstrumentRequest {
    private String CVV;
    private String PIN;

    public CardRequest() {

    }

    public String getCVV() {
        return CVV;
    }

    public String getPIN() {
        return PIN;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }
}
