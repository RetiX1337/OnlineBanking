package org.onlinebankingweb.mappers;

import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.servicedto.BankAccountServiceDTO;
import org.onlinebankingweb.dto.requests.BankAccountCreationRequest;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;
    private final UserService userService;

    public BankAccountMapper(BankAccountService bankAccountService, CustomerService customerService,
                                     UserService userService) {
        this.bankAccountService = bankAccountService;
        this.customerService = customerService;
        this.userService = userService;
    }

    public BankAccountServiceDTO creationRequestToServiceDTO(BankAccountCreationRequest bankAccountCreationRequest,
                                                             UserPrincipal userPrincipal) {
        User user = userService.findByEmail(userPrincipal.getUsername());
        Customer customer = customerService.findByUser(user);
        if (!bankAccountCreationRequest.getCustomerTaxPayerId().equals(customer.getTaxPayerId())) {
            throw new AccessDeniedException("The tax payer ID does not belong to the authenticated customer");
        }
        BankAccountServiceDTO bankAccountServiceDTO = new BankAccountServiceDTO();
        bankAccountServiceDTO.setCustomer(customer);
        return bankAccountServiceDTO;
    }
}