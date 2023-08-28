package org.onlinebanking.core.dataaccess.dao.interfaces;

import org.onlinebanking.core.domain.models.User;

public interface UserDAO extends DAOInterface<User> {
    User findByEmail(String email);
}
