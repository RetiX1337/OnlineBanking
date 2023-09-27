package org.onlinebankingweb.controllers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.domain.servicedto.BankAccountServiceDTO;
import org.onlinebankingweb.dto.requests.BankAccountCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@Controller
@RequestMapping("/bank-account")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService, CustomerService customerService) {
        this.bankAccountService = bankAccountService;
        this.customerService = customerService;
    }

    @PostMapping("/open-account")
    @PreAuthorize("isAuthenticated() && hasRole(USER_ROLE)")
    public String openBankAccount(@ModelAttribute("bankAccountCreationRequest")
                                      BankAccountCreationRequest bankAccountCreationRequest) {
        BankAccountServiceDTO bankAccountServiceDTO = new BankAccountServiceDTO();
        bankAccountServiceDTO.setCustomer(customerService.findByTaxPayerId
                (bankAccountCreationRequest.getCustomerTaxPayerId()));
        bankAccountService.openBankAccount(bankAccountServiceDTO);
        return "";
    }

    @GetMapping("/open-account")
    @PreAuthorize("isAuthenticated() && hasRole(USER_ROLE)")
    public String getBankAccountMenu(Model model) {
        model.addAttribute("bankAccountCreationRequest", new BankAccountCreationRequest());
        return "";
    }
}
