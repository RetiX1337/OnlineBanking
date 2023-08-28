package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.dto.TransactionDTO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;

import java.util.List;

public interface BankAccountService {
    void openBankAccount(Customer customer);
    boolean activateBankAccount(BankAccount bankAccount);
    boolean deactivateBankAccount(BankAccount bankAccount);
    List<BankAccount> findByCustomer(Customer customer);
    BankAccount findByAccountNumber(String accountNumber);
}
