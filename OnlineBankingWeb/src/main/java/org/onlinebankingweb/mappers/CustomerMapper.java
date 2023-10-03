package org.onlinebankingweb.mappers;

import org.onlinebanking.core.domain.models.Customer;
import org.onlinebankingweb.dto.requests.CustomerRegistrationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer registrationRequestToDomain(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = new Customer();
        customer.setAddress(customerRegistrationRequest.getAddress());
        customer.setFirstName(customerRegistrationRequest.getFirstName());
        customer.setLastName(customerRegistrationRequest.getLastName());
        customer.setTaxPayerId(customerRegistrationRequest.getTaxPayerId());
        return customer;
    }
}
