package org.onlinebanking.core.dataaccess.dao.interfaces;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;

import java.util.List;

public interface BankAccountDAO extends DAOInterface<BankAccount> {
    List<BankAccount> findByCustomer(Customer customer);
    BankAccount findByAccountNumber(String accountNumber);
}
