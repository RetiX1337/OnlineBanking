package org.onlinebanking.core.domain.servicedto.paymentinstruments.cards;

import org.onlinebanking.core.domain.servicedto.paymentinstruments.PaymentInstrumentServiceDTO;

import java.util.Date;

public abstract class CardServiceDTO extends PaymentInstrumentServiceDTO {
    protected String CVVHash;
    protected String PINHash;
    protected Date expiryDate;
    protected String cardNumber;

    public CardServiceDTO() {

    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPINHash(String PINHash) {
        this.PINHash = PINHash;
    }

    public void setCVVHash(String CVVHash) {
        this.CVVHash = CVVHash;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPINHash() {
        return PINHash;
    }

    public String getCVVHash() {
        return CVVHash;
    }
}