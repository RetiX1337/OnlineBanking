package org.onlinebankingweb.mappers;

import org.onlinebanking.core.domain.servicedto.UserServiceDTO;
import org.onlinebankingweb.dto.requests.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserServiceDTO registrationRequestToServiceDTO(UserRegistrationRequest userRegistrationRequest) {
        UserServiceDTO userServiceDTO = new UserServiceDTO();
        userServiceDTO.setEmail(userRegistrationRequest.getEmail());
        userServiceDTO.setUsername(userRegistrationRequest.getUsername());
        userServiceDTO.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        return userServiceDTO;
    }
}
