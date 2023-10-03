package org.onlinebankingweb.dto.responses.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;

import java.math.BigDecimal;

public class DebitCardResponse extends CardResponse {
    private final String description;
    private final BigDecimal dailyWithdrawalLimit;
    private final BigDecimal withdrawalCounter;
    private final Integer dailyTransactionLimit;
    private final Integer transactionCounter;

    public DebitCardResponse(DebitCard debitCard) {
        super(debitCard);
        this.dailyWithdrawalLimit = debitCard.getDailyWithdrawalLimit();
        this.withdrawalCounter = debitCard.getWithdrawalCounter();
        this.dailyTransactionLimit = debitCard.getDailyTransactionLimit();
        this.transactionCounter = debitCard.getTransactionCounter();
        this.description = "Debit Card, " + super.toString();
    }

    public BigDecimal getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public BigDecimal getWithdrawalCounter() {
        return withdrawalCounter;
    }

    public Integer getDailyTransactionLimit() {
        return dailyTransactionLimit;
    }

    public Integer getTransactionCounter() {
        return transactionCounter;
    }

    @Override
    public String toString() {
        return description;
    }
}
