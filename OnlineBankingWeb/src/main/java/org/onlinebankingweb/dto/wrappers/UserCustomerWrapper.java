package org.onlinebankingweb.dto.wrappers;

import org.onlinebankingweb.dto.requests.CustomerRegistrationRequest;
import org.onlinebankingweb.dto.requests.UserRegistrationRequest;

import javax.validation.Valid;

public class UserCustomerWrapper {
    @Valid
    private UserRegistrationRequest userRegistrationRequest;
    @Valid
    private CustomerRegistrationRequest customerRegistrationRequest;

    public UserCustomerWrapper(UserRegistrationRequest userRegistrationRequest, CustomerRegistrationRequest customerRegistrationRequest) {
        this.userRegistrationRequest = userRegistrationRequest;
        this.customerRegistrationRequest = customerRegistrationRequest;
    }

    public UserCustomerWrapper() {
    }

    public CustomerRegistrationRequest getCustomerRegistrationRequest() {
        return customerRegistrationRequest;
    }

    public UserRegistrationRequest getUserRegistrationRequest() {
        return userRegistrationRequest;
    }

    public void setCustomerRegistrationRequest(CustomerRegistrationRequest customerRegistrationRequest) {
        this.customerRegistrationRequest = customerRegistrationRequest;
    }

    public void setUserRegistrationRequest(UserRegistrationRequest userRegistrationRequest) {
        this.userRegistrationRequest = userRegistrationRequest;
    }

}
