package org.onlinebankingweb.dto.requests.paymentinstruments.cards;

import java.math.BigDecimal;

public class DebitCardRequest extends CardRequest {
    protected BigDecimal dailyWithdrawalLimit;
    protected Integer dailyTransactionLimit;

    public DebitCardRequest() {

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
