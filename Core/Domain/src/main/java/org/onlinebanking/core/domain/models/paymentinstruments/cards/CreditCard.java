package org.onlinebanking.core.domain.models.paymentinstruments.cards;

import org.onlinebanking.core.domain.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebanking.core.domain.models.BankAccount;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards")
@DiscriminatorValue("credit_card")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public class CreditCard extends Card {
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    public CreditCard(CreditCardRequest creditCardRequest, BankAccount bankAccount) {
        setBankAccount(bankAccount);
        this.creditLimit = creditCardRequest.getCreditLimit();
        setCVVHash(creditCardRequest.getCVVHash());
        setPINHash(creditCardRequest.getPINHash());
        setExpiryDate(creditCardRequest.getExpiryDate());
        setCardNumber(creditCardRequest.getCardNumber());
    }

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
