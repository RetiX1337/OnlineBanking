package org.onlinebanking.core.domain.models.paymentinstruments.cards;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards")
@DiscriminatorValue("credit_card")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public class CreditCard extends Card {
    @Column(name = "credit_limit")
    protected BigDecimal creditLimit;

    public CreditCard() {

    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(creditLimit) == 0 || amount.compareTo(creditLimit) == -1) {
            return super.withdraw(amount);
        } else {
            return false;
        }
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
}
