package org.onlinebanking.core.domain.models.paymentinstruments.cards;

import org.onlinebanking.core.domain.servicedto.paymentinstruments.cards.CreditCardServiceDTO;
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

    public CreditCard(CreditCardServiceDTO creditCardServiceDTO) {
        setBankAccount(creditCardServiceDTO.getBankAccount());
        this.creditLimit = creditCardServiceDTO.getCreditLimit();
        setCVVHash(creditCardServiceDTO.getCVVHash());
        setPINHash(creditCardServiceDTO.getPINHash());
        setExpiryDate(creditCardServiceDTO.getExpiryDate());
        setCardNumber(creditCardServiceDTO.getCardNumber());
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
