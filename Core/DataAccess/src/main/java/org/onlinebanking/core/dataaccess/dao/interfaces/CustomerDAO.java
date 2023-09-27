package org.onlinebanking.core.dataaccess.dao.interfaces;

import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;

public interface CustomerDAO extends DAOInterface<Customer> {
    Customer findByTaxPayerId(String taxPayerId);
    Customer findByUser(User user);
}
