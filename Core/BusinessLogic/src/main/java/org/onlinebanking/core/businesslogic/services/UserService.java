package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.dto.requests.UserRegistrationRequest;
import org.onlinebanking.core.domain.models.user.User;

import java.util.List;

public interface UserService {

    User registerUser(UserRegistrationRequest userRegistrationRequest);
    User findById(Long id);
    User update(User user);
    void delete(User user);
    User findByEmail(String email);
}
