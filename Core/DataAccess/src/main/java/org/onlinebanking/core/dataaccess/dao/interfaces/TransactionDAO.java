package org.onlinebanking.core.dataaccess.dao.interfaces;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.transactions.Transaction;

import java.util.List;

public interface TransactionDAO extends DAOInterface<Transaction> {
    List<Transaction> findBySenderBankAccount(BankAccount bankAccount);
}
