package org.onlinebankingweb.dto.responses.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;

import java.math.BigDecimal;

public class CreditCardResponse extends CardResponse {
    private final String description;
    private final BigDecimal creditLimit;

    public CreditCardResponse(CreditCard creditCard) {
        super(creditCard);
        this.creditLimit = creditCard.getCreditLimit();
        this.description  = "Credit Card, " + super.toString();
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    @Override
    public String toString() {
        return description;
    }
}
