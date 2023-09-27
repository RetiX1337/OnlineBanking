package org.onlinebankingweb.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerRegistrationRequest {
    @NotBlank(message = "First name can't be blank")
    private String firstName;
    @NotBlank(message = "Last name can't be blank")
    private String lastName;
    @NotBlank(message = "Address can't be blank")
    private String address;
    @NotBlank(message = "Tax Payer ID can't be blank")
    @Size(min = 9, message = "Minimum length of Tax Payer ID is 9")
    @Pattern(regexp = "\\b\\d+\\b", message = "The Tax Payer ID can only contain digits")
    private String taxPayerId;

    public CustomerRegistrationRequest() {

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
