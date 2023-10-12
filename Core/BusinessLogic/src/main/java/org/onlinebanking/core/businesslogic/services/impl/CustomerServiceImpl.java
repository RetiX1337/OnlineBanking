package org.onlinebanking.core.businesslogic.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final static Logger logger = LogManager.getLogger(CustomerServiceImpl.class);
    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Transactional
    @Override
    public Customer registerCustomer(Customer customer) {
        if (customer == null || customer.getTaxPayerId() == null) {
            throw new ServiceException();
        }

        String taxPayerId = customer.getTaxPayerId();
        try {
            findByTaxPayerId(taxPayerId);
            throw new FailedCustomerRegistrationException(
                    String.format(FAILED_CUSTOMER_REGISTRATION_EXCEPTION_MESSAGE, taxPayerId));
        } catch (EntityNotFoundException ignored) {
        }

        try {
            return customerDAO.save(customer);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }

    @Transactional
    @Override
    public void deleteCustomer(Customer customer) {
        if (customer == null) {
            throw new ServiceException();
        }

        try {
            customerDAO.delete(customer);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }

    @Transactional
    @Override
    public Customer updateCustomer(Customer customer) {
        if (customer == null) {
            throw new ServiceException();
        }
        try {
            return customerDAO.update(customer);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }

    @Transactional
    @Override
    public boolean assignCustomerToUser(Customer customer, User user) {
        if (customer == null || user == null) {
            throw new ServiceException();
        }

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
        if (taxPayerId == null) {
            throw new ServiceException();
        }

        try {
            return customerDAO.findByTaxPayerId(taxPayerId);
        } catch (NoResultException e) {
            throw new EntityNotFoundException(
                    String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, " taxPayerId " + taxPayerId));
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }

    @Transactional
    @Override
    public Customer findByUser(User user) {
        if (user == null) {
            throw new ServiceException();
        }

        try {
            return customerDAO.findByUser(user);
        } catch (NoResultException e) {
            throw new EntityNotFoundException(
                    String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, " user " + user.getEmail()));
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }
}
