package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.hibernate.query.Query;
import org.onlinebanking.core.dataaccess.dao.interfaces.UserDAO;
import org.onlinebanking.core.domain.models.User;
import org.onlinebanking.core.dataaccess.exceptions.EntityNotFoundException;
import org.onlinebanking.core.dataaccess.exceptions.EntityNotSavedException;
import org.onlinebanking.core.dataaccess.config.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAOHibernateImpl extends DAOHibernateImpl<User> implements UserDAO {

    public UserDAOHibernateImpl(@Autowired HibernateSessionFactory hibernateSessionFactory) {
        super(hibernateSessionFactory, User.class);
    }

    @Override
    public User findByEmail(String email) {
        Session session = hibernateSessionFactory.getSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            String hql = "FROM User WHERE email = :email";
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            User user = (User) query.getResultList().get(0);
            transaction.commit();
            return user;
        } catch (Exception e) {
            transaction.rollback();
            throw new EntityNotFoundException();
        } finally {
            session.close();
        }
    }
}
