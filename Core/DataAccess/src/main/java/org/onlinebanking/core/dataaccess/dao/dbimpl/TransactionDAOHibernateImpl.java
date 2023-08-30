package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.onlinebanking.core.dataaccess.config.HibernateSessionFactory;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionDAOHibernateImpl extends DAOHibernateImpl<Transaction> implements TransactionDAO {

    public TransactionDAOHibernateImpl(@Autowired SessionFactory sessionFactory) {
        super(sessionFactory, Transaction.class);
    }

    @Override
    public List<Transaction> findBySenderBankAccount(BankAccount bankAccount) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Transaction WHERE sender = :sender";
        Query query = session.createQuery(hql);
        query.setParameter("sender", bankAccount);
        return query.getResultList();
    }
}
