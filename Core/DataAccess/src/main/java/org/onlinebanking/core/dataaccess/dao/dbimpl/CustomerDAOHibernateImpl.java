package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.onlinebanking.core.dataaccess.config.HibernateSessionFactory;
import org.onlinebanking.core.dataaccess.dao.interfaces.CustomerDAO;
import org.onlinebanking.core.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerDAOHibernateImpl extends DAOHibernateImpl<Customer> implements CustomerDAO {

    public CustomerDAOHibernateImpl(@Autowired HibernateSessionFactory hibernateSessionFactory) {
        super(hibernateSessionFactory, Customer.class);
    }
}
