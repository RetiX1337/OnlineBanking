package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.dataaccess.dao.interfaces.UserDAO;
import org.onlinebanking.core.domain.dto.UserDTO;
import org.onlinebanking.core.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    public UserServiceImpl(@Autowired UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean registerUser(UserDTO userDTO) {
        if (userDAO.findByEmail(userDTO.getEmail()) != null) {
            return false;
        }
        User user = new User();
        user.setPasswordHash(userDTO.getPasswordHash());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        userDAO.save(user);
        return true;
    }

    @Override
    public boolean loginUser(UserDTO userDTO) {
        User user = userDAO.findByEmail(userDTO.getEmail());
        if (user == null) {
            return false;
        }
        return user.getPasswordHash().equals(userDTO.getPasswordHash());
    }

    @Override
    public User findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public User update(User user) {
        return userDAO.update(user);
    }

    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }
}
