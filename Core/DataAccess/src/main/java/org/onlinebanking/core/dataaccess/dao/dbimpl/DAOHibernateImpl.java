package org.onlinebanking.core.dataaccess.dao.dbimpl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.onlinebanking.core.dataaccess.config.HibernateSessionFactory;
import org.onlinebanking.core.dataaccess.dao.interfaces.DAOInterface;
import org.onlinebanking.core.dataaccess.exceptions.EntityNotFoundException;
import org.onlinebanking.core.dataaccess.exceptions.EntityNotSavedException;
import org.onlinebanking.core.domain.models.Identifiable;
import org.onlinebanking.core.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class DAOHibernateImpl<T extends Identifiable> implements DAOInterface<T> {
    protected final HibernateSessionFactory hibernateSessionFactory;
    protected final Class<T> clazz;

    public DAOHibernateImpl(@Autowired HibernateSessionFactory hibernateSessionFactory, Class<T> clazz) {
        this.hibernateSessionFactory = hibernateSessionFactory;
        this.clazz = clazz;
    }

    @Override
    public void save(T t) {
        Session session = hibernateSessionFactory.getSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            session.persist(t);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            throw new EntityNotSavedException();
        } finally {
            session.close();
        }
    }

    @Override
    public T findById(Long id) {
        Session session = hibernateSessionFactory.getSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            T t = session.get(clazz, id);
            transaction.commit();
            return t;
        } catch (Exception e) {
            transaction.rollback();
            throw new EntityNotFoundException();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(T t) {
        Session session = hibernateSessionFactory.getSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            session.remove(t);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new EntityNotFoundException();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = hibernateSessionFactory.getSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            T t = session.find(clazz, id);
            session.remove(t);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new EntityNotFoundException();
        } finally {
            session.close();
        }
    }

    @Override
    public List<T> findAll() {
        Session session = hibernateSessionFactory.getSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            String hql = "FROM " + clazz.getSimpleName();
            Query query = session.createQuery(hql);
            List<T> list = query.getResultList();
            transaction.commit();
            return list;
        } catch (Exception e) {
            transaction.rollback();
            throw new EntityNotFoundException();
        } finally {
            session.close();
        }
    }

    @Override
    public T update(T t) {
        Session session = hibernateSessionFactory.getSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            T mergedT = (T) session.merge(t);
            transaction.commit();
            return mergedT;
        } catch (Exception e) {
            transaction.rollback();
            throw new EntityNotFoundException();
        } finally {
            session.close();
        }
    }

}
