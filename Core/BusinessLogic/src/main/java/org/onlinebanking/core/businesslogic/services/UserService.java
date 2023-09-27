package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.servicedto.UserServiceDTO;
import org.onlinebanking.core.domain.models.user.User;

public interface UserService {
    User registerUser(UserServiceDTO userServiceDTO);
    User findById(Long id);
    User update(User user);
    void delete(User user);
    User findByEmail(String email);
}
