package org.onlinebanking.core.dataaccess.dao.interfaces;

import org.onlinebanking.core.domain.models.Identifiable;

import java.util.List;

public interface DAOInterface<T extends Identifiable> {
    T save(T t);
    T findById(Long id);
    void delete(T t);
    void deleteById(Long id);
    List<T> findAll();
    T update(T t);
}
