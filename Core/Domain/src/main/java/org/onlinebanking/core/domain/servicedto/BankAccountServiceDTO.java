package org.onlinebanking.core.domain.servicedto;

import org.onlinebanking.core.domain.models.Customer;

public class BankAccountServiceDTO {
    private Customer customer;

    public BankAccountServiceDTO() {

    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
