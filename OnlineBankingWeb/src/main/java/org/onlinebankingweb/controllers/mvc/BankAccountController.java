package org.onlinebankingweb.controllers.mvc;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.servicedto.BankAccountServiceDTO;
import org.onlinebankingweb.dto.requests.BankAccountCreationRequest;
import org.onlinebankingweb.dto.responses.BankAccountResponse;
import org.onlinebankingweb.dto.responses.TransactionResponse;
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponse;
import org.onlinebankingweb.factories.PaymentInstrumentResponseFactory;
import org.onlinebankingweb.mappers.BankAccountMapper;
import org.onlinebankingweb.mappers.PaymentInstrumentMapper;
import org.onlinebankingweb.mappers.TransactionMapper;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Validated
@Controller
@RequestMapping("/bank-account")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BankAccountMapper bankAccountMapper;
    private final TransactionMapper transactionMapper;
    private final PaymentInstrumentMapper paymentInstrumentMapper;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService, PaymentInstrumentMapper paymentInstrumentMapper,
                                 BankAccountMapper bankAccountMapper, TransactionMapper transactionMapper) {
        this.bankAccountService = bankAccountService;
        this.paymentInstrumentMapper = paymentInstrumentMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping("/open-account")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public String openBankAccount(@ModelAttribute("bankAccountCreationRequest") BankAccountCreationRequest bankAccountCreationRequest,
                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        BankAccountServiceDTO bankAccountServiceDTO
                = bankAccountMapper.creationRequestToServiceDTO(bankAccountCreationRequest, userPrincipal);
        bankAccountService.openBankAccount(bankAccountServiceDTO);
        return "redirect:/user/profile";
    }

    @GetMapping("/open-account")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public String getBankAccountMenu(Model model) {
        model.addAttribute("bankAccountCreationRequest", new BankAccountCreationRequest());
        return "bankaccount/open-bank-account";
    }

    @GetMapping("/account")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public String getBankAccount(@RequestParam("accNumber") String accountNumber,
                                 Model model) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(accountNumber);

        model.addAttribute("bankAccountResponse", new BankAccountResponse(bankAccount));
        model.addAttribute("paymentInstrumentResponses", paymentInstrumentMapper.responseListByBankAccount(bankAccount));
        model.addAttribute("transactionResponses", transactionMapper.responseListByBankAccount(bankAccount));
        return "bankaccount/bank-account-page";
    }
}
