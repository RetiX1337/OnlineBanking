package org.onlinebanking.core.domain.servicedto.paymentinstruments.cards;

import java.math.BigDecimal;

public class DebitCardServiceDTO extends CardServiceDTO {
    protected BigDecimal dailyWithdrawalLimit;
    protected Integer dailyTransactionLimit;

    public DebitCardServiceDTO() {

    }

    public void setDailyTransactionLimit(Integer dailyTransactionLimit) {
        this.dailyTransactionLimit = dailyTransactionLimit;
    }

    public void setDailyWithdrawalLimit(BigDecimal dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public Integer getDailyTransactionLimit() {
        return dailyTransactionLimit;
    }

    public BigDecimal getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }
}
