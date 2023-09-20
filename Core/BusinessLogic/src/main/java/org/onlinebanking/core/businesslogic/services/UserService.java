package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.dto.requests.UserRegistrationRequest;
import org.onlinebanking.core.domain.models.user.User;

import java.util.List;

public interface UserService {

    User registerUser(UserRegistrationRequest userRegistrationRequest);
    /*
    boolean loginUser(UserDTO userDTO);
     */
    User findById(Long id);
    void save(User user);
    User update(User user);
    void deleteById(Long id);
    void delete(User user);
    List<User> findAll();
    User findByEmail(String email);
}
