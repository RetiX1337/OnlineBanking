package org.onlinebanking.core.businesslogic.services.impl;

import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.dataaccess.dao.interfaces.CustomerDAO;
import org.onlinebanking.core.domain.exceptions.ServiceException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.FailedCustomerRegistrationException;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final static String FAILED_CUSTOMER_REGISTRATION_EXCEPTION_MESSAGE = "Customer for Tax Payer ID %s already exists";
    private final static String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "Entity not found for %s";
    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Transactional
    @Override
    public Customer registerCustomer(Customer customer) {
        String taxPayerId = customer.getTaxPayerId();
        checkIfCustomerExists(taxPayerId);

        try {
            return customerDAO.save(customer);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public void deleteCustomer(Customer customer) {
        try {
            customerDAO.delete(customer);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Customer updateCustomer(Customer customer) {
        try {
            return customerDAO.update(customer);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public boolean assignCustomerToUser(Customer customer, User user) {
        if (customer.getUser() != null) {
            return false;
        }

        customer.setUser(user);
        updateCustomer(customer);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findByTaxPayerId(String taxPayerId) {
        try {
            return customerDAO.findByTaxPayerId(taxPayerId);
        } catch (NoResultException e) {
            throw new EntityNotFoundException(
                    String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, " taxPayerId " + taxPayerId), e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Customer findByUser(User user) {
        try {
            return customerDAO.findByUser(user);
        } catch (NoResultException e) {
            throw new EntityNotFoundException(
                    String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, " user " + user.getEmail()), e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private void checkIfCustomerExists(String taxPayerId) {
        try {
            findByTaxPayerId(taxPayerId);
            throw new FailedCustomerRegistrationException(
                    String.format(FAILED_CUSTOMER_REGISTRATION_EXCEPTION_MESSAGE, taxPayerId));
        } catch (EntityNotFoundException ignored) {}
    }
}
