package org.onlinebankingweb.controllers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/payment")
@Scope("session")
public class PaymentInstrumentController {
    private final UserService userService;
    private final CustomerService customerService;
    private final BankAccountService bankAccountService;
    private final PaymentInstrumentService paymentInstrumentService;

    @Autowired
    public PaymentInstrumentController(UserService userService, CustomerService customerService, BankAccountService bankAccountService, PaymentInstrumentService paymentInstrumentService) {
        this.userService = userService;
        this.customerService = customerService;
        this.bankAccountService = bankAccountService;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    @GetMapping("/payment-menu")
    public String paymentMenu(@ModelAttribute("accountNumber") String accountNumber, ModelMap model) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(accountNumber);
        List<PaymentInstrument> paymentInstrumentList = paymentInstrumentService.findByBankAccount(bankAccount);
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("paymentInstrumentList", paymentInstrumentList);
        return "paymentinstrument/payment-menu-page";
    }

    @GetMapping("/payment_instrument")
    public String paymentInstrument(@ModelAttribute("id") Long id, Model model) {
        PaymentInstrument paymentInstrument = paymentInstrumentService.findById(id);
        if (paymentInstrument instanceof CreditCard creditCard) {
            model.addAttribute("creditCard", creditCard);
            return "paymentinstrument/credit-card-page";
        } else if (paymentInstrument instanceof DebitCard debitCard) {
            model.addAttribute("debitCard", debitCard);
            return "debit-card-page";
        } else if (paymentInstrument instanceof BankTransfer bankTransfer) {
            model.addAttribute("bankTransfer", bankTransfer);
            return "bank-transfer-page";
        } else {
            return "invalid-payment-instrument-page";
        }
    }
}