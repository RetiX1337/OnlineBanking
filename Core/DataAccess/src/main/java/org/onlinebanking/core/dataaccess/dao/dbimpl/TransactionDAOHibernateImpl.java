package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.onlinebanking.core.dataaccess.config.HibernateSessionFactory;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionDAOHibernateImpl extends DAOHibernateImpl<Transaction> implements TransactionDAO {

    public TransactionDAOHibernateImpl(@Autowired HibernateSessionFactory hibernateSessionFactory) {
        super(hibernateSessionFactory, Transaction.class);
    }
}
