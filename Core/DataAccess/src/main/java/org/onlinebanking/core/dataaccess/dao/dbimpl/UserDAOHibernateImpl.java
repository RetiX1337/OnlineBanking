package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.onlinebanking.core.dataaccess.dao.interfaces.UserDAO;
import org.onlinebanking.core.domain.models.User;
import org.onlinebanking.core.dataaccess.config.HibernateSessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOHibernateImpl extends DAOHibernateImpl<User> implements UserDAO {

    public UserDAOHibernateImpl(@Autowired SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    @Override
    public User findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM User WHERE email = :email";
        Query query = session.createQuery(hql);
        query.setParameter("email", email);
        return (User) query.uniqueResult();
    }
}
