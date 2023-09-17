package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.dataaccess.dao.interfaces.CustomerDAO;
import org.onlinebanking.core.domain.dto.CustomerDTO;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public boolean registerCustomer(CustomerDTO customerDTO) {
        return false;
    }

    @Override
    public void deleteCustomer(Customer customer) {

    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return null;
    }

    @Override
    public boolean assignCustomerToUser(Customer customer, User user) {
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findByTaxPayerId(String taxPayerId) {
        return customerDAO.findByTaxPayerId(taxPayerId);
    }
}
