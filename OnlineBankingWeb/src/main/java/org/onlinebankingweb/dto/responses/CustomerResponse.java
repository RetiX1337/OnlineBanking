package org.onlinebankingweb.dto.responses;

import org.onlinebanking.core.domain.models.Customer;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerResponse that = (CustomerResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(userResponse, that.userResponse) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(address, that.address) && Objects.equals(taxPayerId, that.taxPayerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userResponse, firstName, lastName, address, taxPayerId);
    }
}
