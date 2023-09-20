package org.onlinebankingweb.controllers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
@Scope("session")
public class UserController {
    private final UserService userService;
    private final CustomerService customerService;
    private final BankAccountService bankAccountService;
    private final PaymentInstrumentService paymentInstrumentService;

    @Autowired
    public UserController(UserService userService, CustomerService customerService, BankAccountService bankAccountService, PaymentInstrumentService paymentInstrumentService) {
        this.userService = userService;
        this.customerService = customerService;
        this.bankAccountService = bankAccountService;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    @GetMapping("/starter")
    public String testStarterPage() {
         return "enter-tax-payer";
    }

    @GetMapping("/tax-payer-retrieve")
    public String retrieveCustomer(@RequestParam("tax-payer-value") String value, HttpSession session) {
        Customer customer = customerService.findByTaxPayerId(value);
        session.setAttribute("customer", customer);
        return "bank-account-manipulation";
    }

    @PostMapping("/open-bank-account")
    public String createBankAccount(@SessionAttribute("customer") Customer customer) {
        bankAccountService.openBankAccount(customer);
        return "redirect:/show-bank-accounts";
    }

    @GetMapping("/show-bank-accounts")
    public String getBankAccountList(@SessionAttribute("customer") Customer customer, Model model) {
        model.addAttribute("accounts", bankAccountService.findByCustomer(customer));
        return "bank-accounts";
    }
}
