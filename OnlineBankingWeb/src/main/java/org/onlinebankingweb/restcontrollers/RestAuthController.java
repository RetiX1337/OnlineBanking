package org.onlinebankingweb.restcontrollers;

import org.onlinebankingweb.dto.requests.LoginRequest;
import org.onlinebankingweb.dto.responses.LoginResponse;
import org.onlinebankingweb.security.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestAuthController {
    private final AuthService authService;

    @Autowired
    public RestAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }
}
