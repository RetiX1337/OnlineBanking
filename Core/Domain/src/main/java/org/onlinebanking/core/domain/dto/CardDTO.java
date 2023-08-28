package org.onlinebanking.core.domain.dto;

public abstract class CardDTO extends PaymentInstrumentDTO {
    protected String CVVHash;
    protected String PINHash;

    public CardDTO() {

    }

    public void setPINHash(String PINHash) {
        this.PINHash = PINHash;
    }

    public void setCVVHash(String CVVHash) {
        this.CVVHash = CVVHash;
    }

    public String getPINHash() {
        return PINHash;
    }

    public String getCVVHash() {
        return CVVHash;
    }
}
