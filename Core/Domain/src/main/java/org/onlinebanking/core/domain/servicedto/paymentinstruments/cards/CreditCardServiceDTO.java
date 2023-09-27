package org.onlinebanking.core.domain.servicedto.paymentinstruments.cards;

import java.math.BigDecimal;

public class CreditCardServiceDTO extends CardServiceDTO {
    protected BigDecimal creditLimit;

    public CreditCardServiceDTO() {

    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
}
