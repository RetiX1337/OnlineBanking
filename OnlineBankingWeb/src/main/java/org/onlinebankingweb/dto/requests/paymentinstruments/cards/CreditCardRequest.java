package org.onlinebankingweb.dto.requests.paymentinstruments.cards;

import java.math.BigDecimal;

public class CreditCardRequest extends CardRequest {
    private BigDecimal creditLimit;

    public CreditCardRequest() {

    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
}
