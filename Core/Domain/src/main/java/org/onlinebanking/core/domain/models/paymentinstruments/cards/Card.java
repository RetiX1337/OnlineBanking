package org.onlinebanking.core.domain.models.paymentinstruments.cards;

import javax.persistence.*;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;

import java.util.Date;

@Entity
@Table(name = "cards")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public abstract class Card extends PaymentInstrument {
    @Column(name = "card_number")
    protected String cardNumber;
    @Column(name = "expiry_date")
    protected Date expiryDate;
    @Column(name = "cvv_hash")
    protected String CVVHash;
    @Column(name = "pin_hash")
    protected String PINHash;

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
    public String getDescription() {
        return cardNumber + " " + expiryDate;
    }
}
