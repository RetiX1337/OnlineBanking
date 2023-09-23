package org.onlinebankingweb.controllers;

import org.onlinebanking.core.domain.dto.requests.CustomerRegistrationRequest;
import org.onlinebanking.core.domain.dto.requests.UserRegistrationRequest;
import org.onlinebanking.core.domain.exceptions.FailedCustomerRegistrationException;
import org.onlinebanking.core.domain.exceptions.FailedUserRegistrationException;
import org.onlinebankingweb.security.services.AuthService;
import org.onlinebankingweb.dto.wrappers.UserCustomerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Validated
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest();
        UserCustomerWrapper userCustomerWrapper = new UserCustomerWrapper(userRegistrationRequest, customerRegistrationRequest);
        model.addAttribute("userCustomerWrapper", userCustomerWrapper);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userCustomerWrapper") UserCustomerWrapper userCustomerWrapper,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        String password = userCustomerWrapper.getUserRegistrationRequest().getPassword();
        String passwordHash = passwordEncoder.encode(password);
        userCustomerWrapper.getUserRegistrationRequest().setPassword(passwordHash);
        try {
            authService.attemptRegister(userCustomerWrapper.getUserRegistrationRequest(),
                    userCustomerWrapper.getCustomerRegistrationRequest());
        } catch (FailedCustomerRegistrationException e) {
            model.addAttribute("customerRegistrationExceptionMessage", e.getMessage());
        } catch (FailedUserRegistrationException e) {
            model.addAttribute("userRegistrationExceptionMessage", e.getMessage());
        }
        return "auth/register";
    }
}
