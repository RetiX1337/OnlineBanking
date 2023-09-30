package org.onlinebankingweb.controllers;

import org.dom4j.rule.Mode;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.PaymentInstrumentFactoryException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrumentType;
import org.onlinebankingweb.dto.requests.paymentinstruments.BankTransferRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.PaymentInstrumentRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.DebitCardRequest;
import org.onlinebankingweb.factories.PaymentInstrumentServiceDTOFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Validated
@Controller
@RequestMapping("/payment-instrument")
public class PaymentInstrumentController {
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final PaymentInstrumentService paymentInstrumentService;
    private final PaymentInstrumentServiceDTOFactory paymentInstrumentServiceDTOFactory;

    @Autowired
    public PaymentInstrumentController(BankAccountService bankAccountService, CustomerService customerService,
                                       UserService userService, TransactionService transactionService,
                                       PaymentInstrumentService paymentInstrumentService,
                                       PaymentInstrumentServiceDTOFactory paymentInstrumentServiceDTOFactory) {
        this.bankAccountService = bankAccountService;
        this.customerService = customerService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.paymentInstrumentService = paymentInstrumentService;
        this.paymentInstrumentServiceDTOFactory = paymentInstrumentServiceDTOFactory;
    }

    @GetMapping("/choose-type")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public String choosePaymentInstrumentType(@RequestParam("accNumber") String accountNumber, Model model) {
        model.addAttribute("accountNumber", accountNumber);
        return "paymentinstrument/choose-payment-instrument-type";
    }

    @GetMapping("/open-payment-instrument")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public String getPaymentInstrumentForm(@RequestParam("accNumber") String accountNumber,
                                           @RequestParam("type") String paymentInstrumentType, Model model) {
        addPaymentInstrumentRequest(PaymentInstrumentType.valueOf(paymentInstrumentType), model);
        model.addAttribute("accountNumber", accountNumber);
        return "paymentinstrument/open-payment-instrument";
    }

    @PostMapping("/open-payment-instrument")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public String openPaymentInstrument(@ModelAttribute("paymentInstrumentRequest") PaymentInstrumentRequest paymentInstrumentRequest,
                                        RedirectAttributes redirectAttributes) {
        String redirectWithRequestParam
                = "redirect:/payment-instrument/open-payment-instrument?accNumber=" + paymentInstrumentRequest.getBankAccountNumber();
        try {
            BankAccount bankAccount = bankAccountService.findByAccountNumber
                    (paymentInstrumentRequest.getBankAccountNumber());
            paymentInstrumentService.openPaymentInstrument
                    (paymentInstrumentServiceDTOFactory.createPaymentInstrument(paymentInstrumentRequest, bankAccount));
        } catch (PaymentInstrumentFactoryException e) {
            redirectAttributes.addFlashAttribute("failedTransactionExceptionMessage", e.getMessage());
            return redirectWithRequestParam;
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("entityNotFoundExceptionMessage", e.getMessage());
            return redirectWithRequestParam;
        }

        return "redirect:/user/profile";
    }

    private void addPaymentInstrumentRequest(PaymentInstrumentType paymentInstrumentType, Model model) {
        switch (paymentInstrumentType) {
            case BANK_TRANSFER -> model.addAttribute("paymentInstrumentRequest", new BankTransferRequest());
            case DEBIT_CARD -> model.addAttribute("paymentInstrumentRequest", new DebitCardRequest());
            case CREDIT_CARD -> model.addAttribute("paymentInstrumentRequest", new CreditCardRequest());
        }
    }
}
