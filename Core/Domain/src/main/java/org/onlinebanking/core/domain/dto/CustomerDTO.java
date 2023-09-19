package org.onlinebanking.core.domain.dto;

import org.onlinebanking.core.domain.models.user.User;

public class CustomerDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String taxPayerId;

    public CustomerDTO() {

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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTaxPayerId(String taxPayerId) {
        this.taxPayerId = taxPayerId;
    }
}
