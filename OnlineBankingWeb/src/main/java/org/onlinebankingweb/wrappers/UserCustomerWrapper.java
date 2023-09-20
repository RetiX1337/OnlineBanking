package org.onlinebankingweb.wrappers;

import org.onlinebanking.core.domain.dto.requests.CustomerRegistrationRequest;
import org.onlinebanking.core.domain.dto.requests.UserRegistrationRequest;

public class UserCustomerWrapper {
    private UserRegistrationRequest userRegistrationRequest;
    private CustomerRegistrationRequest customerRegistrationRequest;

    public UserCustomerWrapper(UserRegistrationRequest userRegistrationRequest, CustomerRegistrationRequest customerRegistrationRequest) {
        this.userRegistrationRequest = userRegistrationRequest;
        this.customerRegistrationRequest = customerRegistrationRequest;
    }

    public UserCustomerWrapper() {
    }

    public CustomerRegistrationRequest getCustomerDTO() {
        return customerRegistrationRequest;
    }

    public UserRegistrationRequest getUserDTO() {
        return userRegistrationRequest;
    }

    public void setCustomerDTO(CustomerRegistrationRequest customerRegistrationRequest) {
        this.customerRegistrationRequest = customerRegistrationRequest;
    }

    public void setUserDTO(UserRegistrationRequest userRegistrationRequest) {
        this.userRegistrationRequest = userRegistrationRequest;
    }
}
