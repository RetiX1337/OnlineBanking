package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.onlinebanking.core.dataaccess.dao.interfaces.DAOInterface;
import org.onlinebanking.core.domain.models.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public abstract class DAOHibernateImpl<T extends Identifiable> implements DAOInterface<T> {
    protected final SessionFactory sessionFactory;
    protected final Class<T> clazz;

    public DAOHibernateImpl(@Autowired SessionFactory sessionFactory, Class<T> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    @Override
    public void save(T t) {
        sessionFactory.getCurrentSession().save(t);
    }

    @Transactional
    @Override
    public T findById(Long id) {
        return sessionFactory.getCurrentSession().get(clazz, id);
    }

    @Override
    public void delete(T t) {
        sessionFactory.getCurrentSession().remove(t);
    }

    @Override
    public void deleteById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        T t = session.find(clazz, id);
        session.remove(t);
    }

    @Override
    public List<T> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM " + clazz.getSimpleName();
        Query query = session.createQuery(hql);
        return query.getResultList();
    }

    @Override
    public T update(T t) {
        return  (T) sessionFactory.getCurrentSession().merge(t);
    }

}
