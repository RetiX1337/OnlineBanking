package org.onlinebankingweb.dto.responses.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.cards.Card;

import java.util.Date;

public abstract class CardResponse extends PaymentInstrumentResponse {
    private final String cardNumber;
    private final Date expiryDate;

    public CardResponse(Card card) {
        super(card);
        this.cardNumber = card.getCardNumber();
        this.expiryDate = card.getExpiryDate();
        super.description = "Card Number: " + cardNumber + ", Expiry Date: " + expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        return description;
    }
}
