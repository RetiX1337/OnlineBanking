package org.onlinebanking.core.dataaccess.dao.interfaces;

import org.onlinebanking.core.domain.models.user.User;

public interface UserDAO extends DAOInterface<User> {
    User findByEmail(String email);
}
