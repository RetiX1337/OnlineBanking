package org.onlinebanking.core.dataaccess.dao.interfaces;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.transactions.Transaction;

public interface TransactionDAO extends DAOInterface<Transaction> {
    BankAccount findBySenderBankAccount(BankAccount bankAccount);
}
