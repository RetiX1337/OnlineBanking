package org.onlinebankingweb.rest;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.FailedTransactionException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.servicedto.TransactionServiceDTO;
import org.onlinebankingweb.dto.requests.TransactionRequest;
import org.onlinebankingweb.dto.responses.BankAccountResponse;
import org.onlinebankingweb.dto.responses.TransactionResponse;
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponse;
import org.onlinebankingweb.factories.PaymentInstrumentResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/tx")
public class TransactionRestController {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final PaymentInstrumentService paymentInstrumentService;
    private final CustomerService customerService;
    private final UserService userService;

    @Autowired
    public TransactionRestController(TransactionService transactionService, BankAccountService bankAccountService, PaymentInstrumentService paymentInstrumentService, CustomerService customerService, UserService userService) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.paymentInstrumentService = paymentInstrumentService;
        this.customerService = customerService;
        this.userService = userService;
    }

    @PostMapping("/payment")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public TransactionResponse processPayment(@RequestBody TransactionRequest transactionRequest) {
        return new TransactionResponse(transactionService.processPayment(initTransactionServiceDTO(transactionRequest)));
    }

    private TransactionServiceDTO initTransactionServiceDTO(TransactionRequest transactionRequest) {
        TransactionServiceDTO transactionServiceDTO = new TransactionServiceDTO();
        transactionServiceDTO.setAmount(transactionRequest.getAmount());
        transactionServiceDTO.setTransactionType(transactionRequest.getTransactionType());
        transactionServiceDTO.setReceiver(bankAccountService.findByAccountNumber(transactionRequest.getReceiverBankAccountNumber()));
        transactionServiceDTO.setPaymentInstrument(paymentInstrumentService.findById(Long.valueOf(transactionRequest.getPaymentInstrumentId())));
        transactionServiceDTO.setSender(transactionServiceDTO.getPaymentInstrument().getBankAccount());
        return transactionServiceDTO;
    }
}