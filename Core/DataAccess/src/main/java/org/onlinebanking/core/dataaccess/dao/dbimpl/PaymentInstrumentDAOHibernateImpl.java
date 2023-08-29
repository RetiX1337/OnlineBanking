package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.onlinebanking.core.dataaccess.dao.interfaces.PaymentInstrumentDAO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentInstrumentDAOHibernateImpl extends DAOHibernateImpl<PaymentInstrument> implements PaymentInstrumentDAO {

    public PaymentInstrumentDAOHibernateImpl(@Autowired SessionFactory sessionFactory) {
        super(sessionFactory, PaymentInstrument.class);
    }

    @Override
    public List<PaymentInstrument> findByBankAccount(BankAccount bankAccount) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM PaymentInstrument WHERE bankAccount = :bankAccount";
        Query query = session.createQuery(hql);
        query.setParameter("bankAccount", bankAccount);
        return query.getResultList();
    }

    @Override
    public Card findByCardNumber(String cardNumber) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Card WHERE cardNumber = :cardNumber";
        Query query = session.createQuery(hql);
        query.setParameter("cardNumber", cardNumber);
        return (Card) query.uniqueResult();
    }
}
