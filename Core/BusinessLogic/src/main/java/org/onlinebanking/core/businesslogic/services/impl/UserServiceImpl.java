package org.onlinebanking.core.businesslogic.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.dataaccess.dao.interfaces.UserDAO;
import org.onlinebanking.core.domain.exceptions.ServiceException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.FailedUserRegistrationException;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.models.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final static String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "User couldn't be found by %s";
    private final static String FAILED_USER_REGISTRATION_EXCEPTION_MESSAGE = "User for email address %s already exists";
    private final static Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    public UserServiceImpl(@Autowired UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        if (user == null || user.getEmail() == null) {
            throw new ServiceException();
        }

        String email = user.getEmail();
        try {
            findByEmail(email);
            throw new FailedUserRegistrationException(String.format(FAILED_USER_REGISTRATION_EXCEPTION_MESSAGE, email));
        } catch (EntityNotFoundException ignored) {}

        user.setRoles(List.of(UserRole.USER_ROLE));

        try {
            return userDAO.save(user);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        if (id == null) {
            throw new ServiceException();
        }
        User user;
        try {
            user = userDAO.findById(id);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
        if (user == null) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, " ID " + id));
        }
        return user;
    }

    @Transactional
    @Override
    public User update(User user) {
        if (user == null) {
            throw new ServiceException();
        }
        try {
            return userDAO.update(user);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }

    @Transactional
    @Override
    public void delete(User user) {
        if (user == null) {
            throw new ServiceException();
        }
        try {
            userDAO.delete(user);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        if (email == null) {
            throw new ServiceException();
        }
        try {
            return userDAO.findByEmail(email);
        } catch (NoResultException e) {
            throw new EntityNotFoundException(
                    String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, " email " + email));
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException();
        }
    }
}
