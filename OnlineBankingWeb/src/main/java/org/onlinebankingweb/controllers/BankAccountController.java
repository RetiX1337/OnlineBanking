package org.onlinebankingweb.controllers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.servicedto.BankAccountServiceDTO;
import org.onlinebankingweb.dto.requests.BankAccountCreationRequest;
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

@Validated
@Controller
@RequestMapping("/bank-account")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;
    private final UserService userService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService, CustomerService customerService, UserService userService) {
        this.bankAccountService = bankAccountService;
        this.customerService = customerService;
        this.userService = userService;
    }

    @PostMapping("/open-account")
    @PreAuthorize("isAuthenticated() && hasRole(USER_ROLE)")
    public String openBankAccount(@ModelAttribute("bankAccountCreationRequest") BankAccountCreationRequest bankAccountCreationRequest,
                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.findByEmail(userPrincipal.getUsername());
        Customer customer = customerService.findByUser(user);
        BankAccountServiceDTO bankAccountServiceDTO = new BankAccountServiceDTO();
        bankAccountServiceDTO.setCustomer(customer);
        bankAccountService.openBankAccount(bankAccountServiceDTO);
        return "redirect:/user/profile";
    }

    @GetMapping("/open-account")
    @PreAuthorize("isAuthenticated() && hasRole(USER_ROLE)")
    public String getBankAccountMenu(Model model) {
        model.addAttribute("bankAccountCreationRequest", new BankAccountCreationRequest());
        return "bankaccount/open-bank-account";
    }
}
