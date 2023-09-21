package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.onlinebanking.core.dataaccess.dao.interfaces.CustomerDAO;
import org.onlinebanking.core.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAOHibernateImpl extends DAOHibernateImpl<Customer> implements CustomerDAO {

    public CustomerDAOHibernateImpl(@Autowired SessionFactory sessionFactory) {
        super(sessionFactory, Customer.class);
    }

    @Override
    public Customer findByTaxPayerId(String taxPayerId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Customer WHERE taxPayerId = :taxPayerId";
        Query query = session.createQuery(hql);
        query.setParameter("taxPayerId", taxPayerId);
        return (Customer) query.getSingleResult();
    }
}
