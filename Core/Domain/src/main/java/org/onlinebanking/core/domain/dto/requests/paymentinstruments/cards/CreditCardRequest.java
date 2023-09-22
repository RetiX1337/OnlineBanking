package org.onlinebanking.core.domain.dto.requests.paymentinstruments.cards;

import java.math.BigDecimal;

public class CreditCardRequest extends CardRequest {
    protected BigDecimal creditLimit;

    public CreditCardRequest() {

    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
}
