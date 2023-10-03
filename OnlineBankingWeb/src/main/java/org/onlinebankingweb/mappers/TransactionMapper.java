package org.onlinebankingweb.mappers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.onlinebankingweb.dto.requests.TransactionRequest;
import org.onlinebankingweb.dto.responses.TransactionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final PaymentInstrumentService paymentInstrumentService;

    public TransactionMapper(BankAccountService bankAccountService, TransactionService transactionService,
                             PaymentInstrumentService paymentInstrumentService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    public Transaction requestToDomain(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setReceiver(bankAccountService.findByAccountNumber(transactionRequest.getReceiverBankAccountNumber()));
        transaction.setPaymentInstrument(paymentInstrumentService.findById(Long.valueOf(transactionRequest.getPaymentInstrumentId())));
        transaction.setSender(transaction.getPaymentInstrument().getBankAccount());
        return transaction;
    }

    public List<TransactionResponse> responseListByBankAccountList(List<BankAccount> bankAccountList) {
        return bankAccountList.stream()
                .map(this::responseListByBankAccount)
                .flatMap(List::stream)
                .toList();
    }

    public List<TransactionResponse> responseListByBankAccount(BankAccount bankAccount) {
        return transactionService.findByBankAccount(bankAccount).stream()
                .map(TransactionResponse::new)
                .toList();
    }
}
