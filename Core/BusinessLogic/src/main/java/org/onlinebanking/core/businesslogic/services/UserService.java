package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.dto.UserDTO;
import org.onlinebanking.core.domain.models.user.User;

import java.util.List;

public interface UserService {
    boolean registerUser(UserDTO userDTO);
    boolean loginUser(UserDTO userDTO);
    User findById(Long id);
    void save(User user);
    User update(User user);
    void deleteById(Long id);
    void delete(User user);
    List<User> findAll();
    User findByEmail(String email);
}
