package org.onlinebanking.core.domain.models.paymentinstruments.cards;

import javax.persistence.*;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "cards")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public abstract class Card extends PaymentInstrument {
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "expiry_date")
    private Date expiryDate;
    @Column(name = "cvv_hash")
    private String CVVHash;
    @Column(name = "pin_hash")
    private String PINHash;

    public Card() {

    }

    public void setPINHash(String PINHash) {
        this.PINHash = PINHash;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCVVHash(String CVVHash) {
        this.CVVHash = CVVHash;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPINHash() {
        return PINHash;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCVVHash() {
        return CVVHash;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(cardNumber, card.cardNumber) && Objects.equals(expiryDate, card.expiryDate) && Objects.equals(CVVHash, card.CVVHash) && Objects.equals(PINHash, card.PINHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cardNumber, expiryDate, CVVHash, PINHash);
    }

    @Override
    public String getDescription() {
        return cardNumber + " " + expiryDate;
    }
}
