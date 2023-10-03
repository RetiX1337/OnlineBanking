package org.onlinebankingweb.rest;

import com.google.common.base.Preconditions;
import org.onlinebanking.core.domain.servicedto.CustomerServiceDTO;
import org.onlinebanking.core.domain.servicedto.UserServiceDTO;
import org.onlinebankingweb.dto.requests.CustomerRegistrationRequest;
import org.onlinebankingweb.dto.requests.LoginRequest;
import org.onlinebankingweb.dto.requests.UserRegistrationRequest;
import org.onlinebankingweb.dto.responses.LoginResponse;
import org.onlinebankingweb.mappers.CustomerMapper;
import org.onlinebankingweb.mappers.UserMapper;
import org.onlinebankingweb.security.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final AuthService authService;
    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;

    @Autowired
    public AuthRestController(AuthService authService, UserMapper userMapper, CustomerMapper customerMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
        this.customerMapper = customerMapper;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        Preconditions.checkNotNull(loginRequest);

        return authService.attemptLogin(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public boolean register(@RequestBody UserRegistrationRequest userRegistrationRequest,
                            @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        Preconditions.checkNotNull(userRegistrationRequest);
        Preconditions.checkNotNull(customerRegistrationRequest);

        UserServiceDTO userServiceDTO = userMapper.registrationRequestToServiceDTO(userRegistrationRequest);
        CustomerServiceDTO customerServiceDTO = customerMapper.registrationRequestToServiceDTO(customerRegistrationRequest);

        return authService.attemptRegister(userServiceDTO, customerServiceDTO);
    }
}
