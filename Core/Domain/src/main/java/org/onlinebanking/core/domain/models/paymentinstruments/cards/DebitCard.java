package org.onlinebanking.core.domain.models.paymentinstruments.cards;

import org.onlinebanking.core.domain.dto.requests.DebitCardRequest;
import org.onlinebanking.core.domain.models.BankAccount;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "debit_cards")
@DiscriminatorValue("debit_card")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public class DebitCard extends Card {
    @Column(name = "daily_withdrawal_limit")
    protected BigDecimal dailyWithdrawalLimit;
    @Column(name = "withdrawal_counter")
    private BigDecimal withdrawalCounter;
    @Column(name = "daily_transaction_limit")
    protected Integer dailyTransactionLimit;
    @Column(name = "transaction_counter")
    private Integer transactionCounter;

    public DebitCard() {

    }

    public DebitCard(DebitCardRequest debitCardRequest, BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        this.expiryDate = debitCardRequest.getExpiryDate();
        this.cardNumber = debitCardRequest.getCardNumber();
        this.PINHash = debitCardRequest.getPINHash();
        this.CVVHash = debitCardRequest.getCVVHash();
        this.dailyWithdrawalLimit = debitCardRequest.getDailyWithdrawalLimit();
        this.dailyTransactionLimit = debitCardRequest.getDailyTransactionLimit();
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        boolean isExceedingWithdrawalLimit = amount.compareTo(dailyWithdrawalLimit.subtract(withdrawalCounter)) > 0;

        boolean isExceedingTransactionLimit = dailyTransactionLimit - transactionCounter == 0;

        if (isExceedingWithdrawalLimit) {
            return false;
        }

        if (isExceedingTransactionLimit) {
            return false;
        }

        if (super.withdraw(amount)) {
            transactionCounter++;
            withdrawalCounter = withdrawalCounter.add(amount);
        }

        return true;
    }

    public void setDailyWithdrawalLimit(BigDecimal dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }
    public void setDailyTransactionLimit(Integer dailyTransactionLimit) {
        this.dailyTransactionLimit = dailyTransactionLimit;
    }

    public BigDecimal getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public Integer getDailyTransactionLimit() {
        return dailyTransactionLimit;
    }
}
