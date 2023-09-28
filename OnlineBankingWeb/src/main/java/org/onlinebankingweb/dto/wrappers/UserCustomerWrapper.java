package org.onlinebankingweb.dto.wrappers;

import org.onlinebanking.core.domain.servicedto.CustomerServiceDTO;
import org.onlinebanking.core.domain.servicedto.UserServiceDTO;

import javax.validation.Valid;

public class UserCustomerWrapper {
    @Valid
    private UserServiceDTO userServiceDTO;
    @Valid
    private CustomerServiceDTO customerServiceDTO;

    public UserCustomerWrapper(UserServiceDTO userServiceDTO, CustomerServiceDTO customerServiceDTO) {
        this.userServiceDTO = userServiceDTO;
        this.customerServiceDTO = customerServiceDTO;
    }

    public UserCustomerWrapper() {
    }

    public CustomerServiceDTO getCustomerRegistrationRequest() {
        return customerServiceDTO;
    }

    public UserServiceDTO getUserRegistrationRequest() {
        return userServiceDTO;
    }

    public void setCustomerRegistrationRequest(CustomerServiceDTO customerServiceDTO) {
        this.customerServiceDTO = customerServiceDTO;
    }

    public void setUserRegistrationRequest(UserServiceDTO userServiceDTO) {
        this.userServiceDTO = userServiceDTO;
    }

}
