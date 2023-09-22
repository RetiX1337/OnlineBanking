package org.onlinebankingweb.dto.responses;

import org.onlinebanking.core.domain.models.Customer;

public class CustomerResponse {
    private final Long id;
    private final UserResponse userResponse;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String taxPayerId;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.userResponse = new UserResponse(customer.getUser());
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.address = customer.getAddress();
        this.taxPayerId = customer.getTaxPayerId();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getTaxPayerId() {
        return taxPayerId;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }
}
