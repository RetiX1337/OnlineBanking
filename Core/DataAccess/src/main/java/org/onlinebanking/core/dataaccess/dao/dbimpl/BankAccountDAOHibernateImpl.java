package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.onlinebanking.core.dataaccess.dao.interfaces.BankAccountDAO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BankAccountDAOHibernateImpl extends DAOHibernateImpl<BankAccount> implements BankAccountDAO {

    public BankAccountDAOHibernateImpl(@Autowired SessionFactory sessionFactory) {
        super(sessionFactory, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByCustomer(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM BankAccount WHERE accountHolder = :accountHolder";
        Query<BankAccount> query = session.createQuery(hql);
        query.setParameter("accountHolder", customer);
        return query.getResultList();
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM BankAccount WHERE accountNumber = :accountNumber";
        Query query = session.createQuery(hql);
        query.setParameter("accountNumber", accountNumber);
        return (BankAccount) query.uniqueResult();
    }
}
