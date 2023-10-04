package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.transactions.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction processPayment(Transaction transaction);
    List<Transaction> findByBankAccount(BankAccount bankAccount);
    Transaction findById(Long id);

}
