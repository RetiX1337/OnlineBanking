package org.onlinebankingweb.mappers;

import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.models.user.UserRole;
import org.onlinebankingweb.dto.requests.UserRegistrationRequest;
import org.onlinebankingweb.dto.requests.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User registrationRequestToDomain(UserRegistrationRequest userRegistrationRequest) {
        User user = new User();
        user.setEmail(userRegistrationRequest.getEmail());
        user.setUsername(userRegistrationRequest.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        return user;
    }

    public User updateRequestToDomain(UserUpdateRequest userUpdateRequest, Long id, List<UserRole> roles) {
        User user = new User();
        user.setUsername(userUpdateRequest.getUsername());
        user.setEmail(userUpdateRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userUpdateRequest.getPassword()));
        user.setId(id);
        user.setRoles(roles);
        return user;
    }
}
