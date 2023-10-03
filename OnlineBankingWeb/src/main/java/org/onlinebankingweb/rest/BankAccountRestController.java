package org.onlinebankingweb.rest;

import com.google.common.base.Preconditions;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.servicedto.BankAccountServiceDTO;
import org.onlinebankingweb.dto.requests.BankAccountCreationRequest;
import org.onlinebankingweb.dto.responses.BankAccountResponse;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank-account")
public class BankAccountRestController {
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;
    private final UserService userService;

    public BankAccountRestController(BankAccountService bankAccountService, CustomerService customerService,
                                     UserService userService) {
        this.bankAccountService = bankAccountService;
        this.customerService = customerService;
        this.userService = userService;
    }

    @GetMapping("/{bankAccountNumber}")
    public BankAccountResponse getBankAccount(@PathVariable("bankAccountNumber") String bankAccountNumber) {
        Preconditions.checkNotNull(bankAccountNumber);

        return new BankAccountResponse(bankAccountService.findByAccountNumber(bankAccountNumber));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public BankAccountResponse addBankAccount(@RequestBody BankAccountCreationRequest bankAccountCreationRequest,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(bankAccountCreationRequest);

        BankAccountServiceDTO bankAccountServiceDTO = new BankAccountServiceDTO();
        bankAccountServiceDTO.setCustomer(customerService.findByUser
                (userService.findByEmail(userPrincipal.getUsername())));

        return new BankAccountResponse(bankAccountService.openBankAccount(bankAccountServiceDTO));
    }
}
