package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;

import java.util.List;

public interface BankAccountService {
    BankAccount openBankAccount(BankAccount bankAccount);
    boolean activateBankAccount(BankAccount bankAccount);
    boolean deactivateBankAccount(BankAccount bankAccount);
    BankAccount updateBankAccount(BankAccount bankAccount);
    List<BankAccount> findByCustomer(Customer customer);
    BankAccount findByAccountNumber(String accountNumber);
}
