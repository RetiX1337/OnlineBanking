package org.onlinebankingweb.dto.requests;

public class BankAccountCreationRequest {
    private String taxPayerId;

    public BankAccountCreationRequest() {

    }

    public String getCustomerTaxPayerId() {
        return taxPayerId;
    }

    public void setCustomerTaxPayerId(String taxPayerId) {
        this.taxPayerId = taxPayerId;
    }
}
