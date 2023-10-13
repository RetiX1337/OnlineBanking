package org.onlinebankingweb.mappers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebankingweb.dto.requests.BankAccountCreationRequest;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {
    private final CustomerService customerService;
    private final UserService userService;

    public BankAccountMapper(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    public BankAccount creationRequestToDomain(BankAccountCreationRequest bankAccountCreationRequest,
                                                   UserPrincipal userPrincipal) {
        User user = userService.findByEmail(userPrincipal.getUsername());
        Customer customer = customerService.findByUser(user);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder(customer);
        return bankAccount;
    }
}
