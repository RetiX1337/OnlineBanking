package org.onlinebanking.core.domain.models.paymentinstruments;

import org.onlinebanking.core.domain.dto.BankTransferDTO;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_transfer")
@DiscriminatorValue("bank_transfer")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public class BankTransfer extends PaymentInstrument {
    @Column(name = "daily_transaction_limit")
    private Integer dailyTransactionLimit;
    @Column(name = "transaction_counter")
    private Integer transactionCounter;

    public BankTransfer(BankTransferDTO bankTransferDTO) {
        this.bankAccount = bankTransferDTO.getBankAccount();
        this.dailyTransactionLimit = bankTransferDTO.getDailyTransactionLimit();
    }

    public BankTransfer() {

    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        boolean isExceedingTransactionLimit = dailyTransactionLimit - transactionCounter == 0;

        if (isExceedingTransactionLimit) {
            return false;
        }

        if (super.withdraw(amount)) {
            transactionCounter++;
        }

        return true;
    }

    public Integer getDailyTransactionLimit() {
        return dailyTransactionLimit;
    }

    public void setDailyTransactionLimit(Integer dailyTransactionLimit) {
        this.dailyTransactionLimit = dailyTransactionLimit;
    }
}
