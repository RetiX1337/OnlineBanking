package org.onlinebankingweb.controllers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.dto.requests.TransactionRequest;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/tx")
@Scope("session")
public class TransactionController {
    private final UserService userService;
    private final CustomerService customerService;
    private final BankAccountService bankAccountService;
    private final PaymentInstrumentService paymentInstrumentService;
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(UserService userService, CustomerService customerService, BankAccountService bankAccountService, PaymentInstrumentService paymentInstrumentService, TransactionService transactionService) {
        this.userService = userService;
        this.customerService = customerService;
        this.bankAccountService = bankAccountService;
        this.paymentInstrumentService = paymentInstrumentService;
        this.transactionService = transactionService;
    }

    @GetMapping("/transaction-form")
    public String getTransactionForm(@ModelAttribute("paymentInstrumentId") Long paymentInstrumentId, Model model) {
        PaymentInstrument paymentInstrument = paymentInstrumentService.findById(paymentInstrumentId);
        model.addAttribute("paymentInstrument", paymentInstrument);
        model.addAttribute("bankAccountSender", paymentInstrument.getBankAccount());
        return "/tx/transaction-form-page";
    }

    @PostMapping("/process-transaction")
    public String processTransaction(@ModelAttribute("paymentInstrumentId") Long paymentInstrumentId,
                                      @ModelAttribute("receiverBankAccountNumber") String receiverBankAccountNumber,
                                      @ModelAttribute("amount") BigDecimal amount,
                                     Model model) {
        TransactionRequest transactionRequest = new TransactionRequest();
        PaymentInstrument paymentInstrument = paymentInstrumentService.findById(paymentInstrumentId);
        BankAccount sender = paymentInstrument.getBankAccount();
        BankAccount receiver = bankAccountService.findByAccountNumber(receiverBankAccountNumber);

        transactionRequest.setPaymentInstrument(paymentInstrument);
        transactionRequest.setSender(sender);
        transactionRequest.setReceiver(receiver);
        transactionRequest.setAmount(amount);

        model.addAttribute("bankAccountNumber", sender.getAccountNumber());

        return "forward:/transaction-history";
    }

    @GetMapping("/transaction-history")
    public String transactionHistory(@ModelAttribute("bankAccountNumber") String bankAccountNumber,
                                     Model model) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(bankAccountNumber);
        List<Transaction> transactionList = transactionService.findByBankAccount(bankAccount);
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("bankAccount", bankAccount);

        return "/tx/transaction-list";
    }
}
