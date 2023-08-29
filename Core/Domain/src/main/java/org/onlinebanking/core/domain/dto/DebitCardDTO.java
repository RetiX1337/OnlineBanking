package org.onlinebanking.core.domain.dto;

import java.math.BigDecimal;

public class DebitCardDTO extends CardDTO {
    protected BigDecimal dailyWithdrawalLimit;
    protected Integer dailyTransactionLimit;

    public DebitCardDTO() {

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
