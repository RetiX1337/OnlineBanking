package org.onlinebankingweb.mappers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.servicedto.TransactionServiceDTO;
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

    public TransactionServiceDTO requestToServiceDTO(TransactionRequest transactionRequest) {
        TransactionServiceDTO transactionServiceDTO = new TransactionServiceDTO();
        transactionServiceDTO.setAmount(transactionRequest.getAmount());
        transactionServiceDTO.setTransactionType(transactionRequest.getTransactionType());
        transactionServiceDTO.setReceiver(bankAccountService.findByAccountNumber(transactionRequest.getReceiverBankAccountNumber()));
        transactionServiceDTO.setPaymentInstrument(paymentInstrumentService.findById(Long.valueOf(transactionRequest.getPaymentInstrumentId())));
        transactionServiceDTO.setSender(transactionServiceDTO.getPaymentInstrument().getBankAccount());
        return transactionServiceDTO;
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
