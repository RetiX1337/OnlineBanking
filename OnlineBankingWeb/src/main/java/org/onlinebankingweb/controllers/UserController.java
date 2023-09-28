package org.onlinebankingweb.controllers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebankingweb.dto.responses.BankAccountResponse;
import org.onlinebankingweb.dto.responses.CustomerResponse;
import org.onlinebankingweb.dto.responses.TransactionResponse;
import org.onlinebankingweb.dto.responses.UserResponse;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Validated
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final CustomerService customerService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    @Autowired
    public UserController(UserService userService, CustomerService customerService,
                          BankAccountService bankAccountService, TransactionService transactionService) {
        this.userService = userService;
        this.customerService = customerService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/profile")
    public String getUserPage(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        User user = userService.findByEmail(userPrincipal.getUsername());
        Customer customer = customerService.findByUser(user);
        UserResponse userResponse = new UserResponse(user);
        CustomerResponse customerResponse = new CustomerResponse(customer);

        List<BankAccount> bankAccountList = bankAccountService.findByCustomer(customer);

        List<BankAccountResponse> bankAccountResponses = bankAccountList.stream()
                .map(BankAccountResponse::new)
                .toList();

        List<TransactionResponse> transactionResponses = bankAccountList.stream()
                .map(transactionService::findByBankAccount)
                .flatMap(List::stream)
                .map(TransactionResponse::new)
                .toList();

        List<TransactionResponse> incomingTransactions = transactionResponses.stream()
                .filter(t -> bankAccountResponses.contains(t.getReceiver()))
                .toList();

        List<TransactionResponse> outgoingTransactions = transactionResponses.stream()
                .filter(t -> bankAccountResponses.contains(t.getSender()))
                .toList();

        model.addAttribute("userResponse", userResponse);
        model.addAttribute("customerResponse", customerResponse);
        model.addAttribute("bankAccountResponses", bankAccountResponses);
        model.addAttribute("transactionResponses", transactionResponses);
        model.addAttribute("incomingTransactions", incomingTransactions);
        model.addAttribute("outgoingTransactions", outgoingTransactions);
        return "user/user-profile";
    }
}
