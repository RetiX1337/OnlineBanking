package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.User;

public interface CustomerService {
    boolean registerCustomer();
    void deleteCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    boolean assignCustomerToUser(Customer customer, User user);
    Customer findByTaxPayerId(String taxPayerId);
}
