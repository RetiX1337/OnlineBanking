package org.onlinebankingweb.controllers.rest;

import com.google.common.base.Preconditions;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.servicedto.BankAccountServiceDTO;
import org.onlinebankingweb.dto.requests.BankAccountCreationRequest;
import org.onlinebankingweb.dto.responses.BankAccountResponse;
import org.onlinebankingweb.mappers.BankAccountMapper;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final BankAccountMapper bankAccountMapper;

    public BankAccountRestController(BankAccountService bankAccountService, BankAccountMapper bankAccountMapper) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
    }

    @GetMapping("/{bankAccountNumber}")
    public BankAccountResponse getBankAccount(@PathVariable("bankAccountNumber") String bankAccountNumber,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(bankAccountNumber);
        BankAccount bankAccount = bankAccountService.findByAccountNumber(bankAccountNumber);

        if (bankAccount.getAccountHolder().getUser().getId().equals(userPrincipal.getUserId())) {
            return new BankAccountResponse(bankAccount);
        } else {
            throw new AccessDeniedException("The Bank Account doesn't belong to the logged in Customer");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public BankAccountResponse addBankAccount(@RequestBody BankAccountCreationRequest bankAccountCreationRequest,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(bankAccountCreationRequest);

        BankAccountServiceDTO bankAccountServiceDTO
                = bankAccountMapper.creationRequestToServiceDTO(bankAccountCreationRequest, userPrincipal);
        return new BankAccountResponse(bankAccountService.openBankAccount(bankAccountServiceDTO));
    }
}
