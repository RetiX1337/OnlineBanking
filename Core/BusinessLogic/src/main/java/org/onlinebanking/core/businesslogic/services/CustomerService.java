package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;

public interface CustomerService {
    Customer registerCustomer(Customer customer);
    void deleteCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    boolean assignCustomerToUser(Customer customer, User user);
    Customer findByTaxPayerId(String taxPayerId);
    Customer findByUser(User user);
}
