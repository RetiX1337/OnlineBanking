package org.onlinebankingweb.controllers.mvc;

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
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponse;
import org.onlinebankingweb.factories.PaymentInstrumentResponseFactory;
import org.onlinebankingweb.mappers.PaymentInstrumentMapper;
import org.onlinebankingweb.mappers.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Validated
@Controller
@RequestMapping("/tx")
public class TransactionController {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final TransactionMapper transactionMapper;
    private final PaymentInstrumentMapper paymentInstrumentMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, BankAccountService bankAccountService,
                                 TransactionMapper transactionMapper, PaymentInstrumentMapper paymentInstrumentMapper) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.transactionMapper = transactionMapper;
        this.paymentInstrumentMapper = paymentInstrumentMapper;
    }

    @GetMapping("/payment")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public String getTransactionForm(@RequestParam("accNumber") String bankAccountNumber,
                                     Model model) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(bankAccountNumber);

        model.addAttribute("paymentInstrumentResponses", paymentInstrumentMapper.responseListByBankAccount(bankAccount));
        model.addAttribute("bankAccountResponse", new BankAccountResponse(bankAccount));
        model.addAttribute("transactionRequest", new TransactionRequest());
        return "tx/transaction-form";
    }

    @PostMapping("/payment")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public String processPayment(@ModelAttribute TransactionRequest transactionRequest, BindingResult result,
                                       RedirectAttributes redirectAttributes) {
        String redirectWithRequestParam = "redirect:/tx/payment?accNumber=" + transactionRequest.getSenderBankAccountNumber();
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("result", result);
            return redirectWithRequestParam;
        }
        try {
            transactionService.processPayment(transactionMapper.requestToServiceDTO(transactionRequest));
        } catch (FailedTransactionException e) {
            redirectAttributes.addFlashAttribute("failedTransactionExceptionMessage", e.getMessage());
            return redirectWithRequestParam;
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("entityNotFoundExceptionMessage", e.getMessage());
            return redirectWithRequestParam;
        }

        return "redirect:/user/profile";
    }

}
