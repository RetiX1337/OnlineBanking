package org.onlinebanking.core.domain.models;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Table(name = "bank_accounts")
public class BankAccount implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private Long id;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "account_balance")
    private BigDecimal accountBalance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer accountHolder;
    @Column(name = "is_active")
    private boolean isActive;

    public boolean withdraw(BigDecimal amount) {
        int isAccountBalanceEnough = amount.compareTo(accountBalance);
        if (isAccountBalanceEnough == 0 || isAccountBalanceEnough == -1) {
            accountBalance = accountBalance.subtract(amount);
            return true;
        }
        return false;
    }

    public void deposit(BigDecimal amount) {
        accountBalance = accountBalance.add(amount);
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setAccountHolder(Customer accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void activateBankAccount() {
        isActive = true;
    }

    public void deactivateBankAccount() {
        isActive = false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Customer getAccountHolder() {
        return accountHolder;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return isActive == that.isActive && Objects.equals(id, that.id) && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(accountBalance, that.accountBalance) && Objects.equals(accountHolder, that.accountHolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, accountBalance, accountHolder, isActive);
    }
}
