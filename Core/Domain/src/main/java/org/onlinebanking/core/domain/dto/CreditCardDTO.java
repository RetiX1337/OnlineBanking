package org.onlinebanking.core.domain.dto;

import java.math.BigDecimal;

public class CreditCardDTO extends CardDTO {
    protected BigDecimal creditLimit;

    public CreditCardDTO() {

    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
}
