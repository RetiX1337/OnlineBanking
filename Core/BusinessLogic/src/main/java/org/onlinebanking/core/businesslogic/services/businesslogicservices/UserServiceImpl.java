package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.dataaccess.dao.interfaces.UserDAO;
import org.onlinebanking.core.domain.dto.requests.UserRegistrationRequest;
import org.onlinebanking.core.domain.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    public UserServiceImpl(@Autowired UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    @Override
    public User registerUser(UserRegistrationRequest userRegistrationRequest) {
        if (userDAO.findByEmail(userRegistrationRequest.getEmail()) != null) {
            // throw exception here
        }
        User user = new User();
        user.setPasswordHash(userRegistrationRequest.getPassword());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setUsername(userRegistrationRequest.getUsername());
        return userDAO.save(user);
    }

    /*
    @Transactional
    @Override
    public boolean loginUser(UserDTO userDTO) {
        User user = userDAO.findByEmail(userDTO.getEmail());
        if (user == null) {
            return false;
        }
        return user.getPasswordHash().equals(userDTO.getPasswordHash());
    }
     */

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userDAO.findById(id);
    }

    @Transactional
    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        return userDAO.update(user);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }
}
