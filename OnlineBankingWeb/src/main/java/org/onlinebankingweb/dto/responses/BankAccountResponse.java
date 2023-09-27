package org.onlinebankingweb.dto.responses;

import org.onlinebanking.core.domain.models.BankAccount;

import java.math.BigDecimal;
import java.util.Objects;

public class BankAccountResponse {
    private final String accountNumber;
    private final BigDecimal accountBalance;
    private final CustomerResponse accountHolder;
    private final boolean isActive;

    public BankAccountResponse(BankAccount bankAccount) {
        this.accountNumber = bankAccount.getAccountNumber();
        this.accountBalance = bankAccount.getAccountBalance();
        this.accountHolder = new CustomerResponse(bankAccount.getAccountHolder());
        this.isActive = bankAccount.isActive();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public CustomerResponse getAccountHolder() {
        return accountHolder;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountResponse that = (BankAccountResponse) o;
        return isActive == that.isActive && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(accountBalance, that.accountBalance) && Objects.equals(accountHolder, that.accountHolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountBalance, accountHolder, isActive);
    }
}
