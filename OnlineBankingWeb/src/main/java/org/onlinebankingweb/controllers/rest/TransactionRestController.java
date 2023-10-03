package org.onlinebankingweb.controllers.rest;

import com.google.common.base.Preconditions;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebankingweb.dto.requests.TransactionRequest;
import org.onlinebankingweb.dto.responses.TransactionResponse;
import org.onlinebankingweb.mappers.TransactionMapper;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tx")
public class TransactionRestController {
    private final UserService userService;
    private final CustomerService customerService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionRestController(TransactionService transactionService, TransactionMapper transactionMapper,
                                     BankAccountService bankAccountService, UserService userService,
                                     CustomerService customerService) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.bankAccountService = bankAccountService;
        this.userService = userService;
        this.customerService = customerService;
    }

    @PostMapping("/payment")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public TransactionResponse processPayment(@RequestBody TransactionRequest transactionRequest) {
        Preconditions.checkNotNull(transactionRequest);

        return new TransactionResponse(transactionService.processPayment(
                transactionMapper.requestToDomain(transactionRequest)
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public TransactionResponse getTransactionResponse(@PathVariable("id") Long id,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(id);

        Transaction transaction = transactionService.findById(id);
        List<Long> receiverAndSenderIds = List.of(transaction.getReceiver().getAccountHolder().getUser().getId(), transaction.getSender().getAccountHolder().getUser().getId());
        if (receiverAndSenderIds.contains(userPrincipal.getUserId())) {
            return new TransactionResponse(transaction);
        } else {
            throw new AccessDeniedException("The Transaction is not related to logged in Customer");
        }
    }

    @GetMapping("/accounts/{accountNumber}")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public List<TransactionResponse> getTransactionResponseList(@PathVariable("accountNumber") String accountNumber,
                                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(accountNumber);

        BankAccount bankAccount = bankAccountService.findByAccountNumber(accountNumber);
        if (bankAccount.getAccountHolder().getUser().getId().equals(userPrincipal.getUserId())) {
            return transactionMapper.responseListByBankAccount(bankAccount);
        } else {
            throw new AccessDeniedException("The Bank Account is not related to logged in Customer");
        }
    }

    @GetMapping("/accounts")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public List<TransactionResponse> getTransactionResponseList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getUserId());
        Customer customer = customerService.findByUser(user);
        List<BankAccount> bankAccount = bankAccountService.findByCustomer(customer);
        return transactionMapper.responseListByBankAccountList(bankAccount);
    }
}