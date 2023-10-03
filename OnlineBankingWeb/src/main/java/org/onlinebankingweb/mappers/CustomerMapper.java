package org.onlinebankingweb.mappers;

import org.onlinebanking.core.domain.servicedto.CustomerServiceDTO;
import org.onlinebankingweb.dto.requests.CustomerRegistrationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerServiceDTO registrationRequestToServiceDTO(CustomerRegistrationRequest customerRegistrationRequest) {
        CustomerServiceDTO customerServiceDTO = new CustomerServiceDTO();
        customerServiceDTO.setAddress(customerRegistrationRequest.getAddress());
        customerServiceDTO.setFirstName(customerRegistrationRequest.getFirstName());
        customerServiceDTO.setLastName(customerRegistrationRequest.getLastName());
        customerServiceDTO.setTaxPayerId(customerRegistrationRequest.getTaxPayerId());
        return customerServiceDTO;
    }
}
