package org.onlinebanking.core.domain.models.paymentinstruments.cards;

import org.onlinebanking.core.domain.dto.DebitCardDTO;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrumentType;

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

    public DebitCard(DebitCardDTO debitCardDTO) {
        this.bankAccount = debitCardDTO.getBankAccount();
        this.expiryDate = debitCardDTO.getExpiryDate();
        this.cardNumber = debitCardDTO.getCardNumber();
        this.PINHash = debitCardDTO.getPINHash();
        this.CVVHash = debitCardDTO.getCVVHash();
        this.dailyWithdrawalLimit = debitCardDTO.getDailyWithdrawalLimit();
        this.dailyTransactionLimit = debitCardDTO.getDailyTransactionLimit();
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

    @Override
    public PaymentInstrumentType getPaymentInstrumentType() {
        return PaymentInstrumentType.DEBIT_CARD;
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
