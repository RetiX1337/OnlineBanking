package org.onlinebanking.core.domain.dto;

public class BankTransferDTO extends PaymentInstrumentDTO {
    private Integer dailyTransactionLimit;

    public BankTransferDTO() {}

    public void setDailyTransactionLimit(Integer dailyTransactionLimit) {
        this.dailyTransactionLimit = dailyTransactionLimit;
    }

    public Integer getDailyTransactionLimit() {
        return dailyTransactionLimit;
    }
}
