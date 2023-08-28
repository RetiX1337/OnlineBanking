package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.onlinebanking.core.dataaccess.config.HibernateSessionFactory;
import org.onlinebanking.core.dataaccess.dao.interfaces.BankAccountDAO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankAccountDAOHibernateImpl extends DAOHibernateImpl<BankAccount> implements BankAccountDAO {

    public BankAccountDAOHibernateImpl(@Autowired HibernateSessionFactory hibernateSessionFactory) {
        super(hibernateSessionFactory, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByCustomer(Customer customer) {
        return null;
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return null;
    }
}
