package org.onlinebankingweb.controllers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.FailedTransactionException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.transactions.TransactionStatus;
import org.onlinebanking.core.domain.models.transactions.TransactionType;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.servicedto.TransactionServiceDTO;
import org.onlinebankingweb.dto.requests.TransactionRequest;
import org.onlinebankingweb.dto.responses.BankAccountResponse;
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponse;
import org.onlinebankingweb.factories.PaymentInstrumentResponseFactory;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Validated
@Controller
@RequestMapping("/tx")
public class TransactionController {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final PaymentInstrumentService paymentInstrumentService;
    private final CustomerService customerService;
    private final UserService userService;

    @Autowired
    public TransactionController(TransactionService transactionService, BankAccountService bankAccountService, PaymentInstrumentService paymentInstrumentService, CustomerService customerService, UserService userService) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.paymentInstrumentService = paymentInstrumentService;
        this.customerService = customerService;
        this.userService = userService;
    }
//
//    @GetMapping("/payment")
//    @PreAuthorize("isAuthenticated() && hasRole(USER_ROLE)")
//    public String getTransactionForm(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
//        User user = userService.findByEmail(userPrincipal.getUsername());
//        Customer customer = customerService.findByUser(user);
//        List<BankAccount> bankAccounts = bankAccountService.findByCustomer(customer);
//
//        Map<BankAccountResponse, List<PaymentInstrumentResponse>> bankAccountPaymentInstrumentResponseMap
//                = bankAccounts.stream()
//                .collect(Collectors.toMap(BankAccountResponse::new,
//                        bankAccount -> paymentInstrumentService.findByBankAccount(bankAccount)
//                                .stream()
//                                .map(PaymentInstrumentResponseFactory::createPaymentInstrument)
//                                .toList()));
//
//        model.addAttribute("bankAccountPaymentInstrumentResponseMap", bankAccountPaymentInstrumentResponseMap);
//        model.addAttribute("transactionRequest", new TransactionRequest());
//        return "tx/transaction-form";
//    }

    @GetMapping("/payment")
    @PreAuthorize("isAuthenticated() && hasRole(USER_ROLE)")
    public String getTransactionForm(@RequestParam("accNumber") String bankAccountNumber,
                                     Model model) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(bankAccountNumber);

        List<PaymentInstrumentResponse> paymentInstrumentResponses
                = paymentInstrumentService.findByBankAccount(bankAccount)
                        .stream()
                        .map(PaymentInstrumentResponseFactory::createPaymentInstrument)
                        .toList();

        System.out.println(model.getAttribute("entityNotFoundExceptionMessage"));
        model.addAttribute("paymentInstrumentResponses", paymentInstrumentResponses);
        model.addAttribute("bankAccountResponse", new BankAccountResponse(bankAccount));
        model.addAttribute("transactionRequest", new TransactionRequest());
        return "tx/transaction-form";
    }

    @PostMapping("/payment")
    @PreAuthorize("isAuthenticated() && hasRole(USER_ROLE)")
    public String processPayment(@ModelAttribute TransactionRequest transactionRequest, BindingResult result,
                                       RedirectAttributes redirectAttributes) {
        String redirectWithRequestParam = "redirect:/tx/payment?accNumber=" + transactionRequest.getSenderBankAccountNumber();
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("result", result);
            return redirectWithRequestParam;
        }
        try {
            transactionService.processPayment(initTransactionServiceDTO(transactionRequest));
        } catch (FailedTransactionException e) {
            redirectAttributes.addFlashAttribute("failedTransactionExceptionMessage", e.getMessage());
            return redirectWithRequestParam;
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("entityNotFoundExceptionMessage", e.getMessage());
            return redirectWithRequestParam;
        }

        return "redirect:/user/profile";
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
