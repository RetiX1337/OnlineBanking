package org.onlinebankingweb.controllers;

import org.onlinebanking.core.domain.dto.CustomerDTO;
import org.onlinebanking.core.domain.dto.UserDTO;
import org.onlinebankingweb.security.services.AuthService;
import org.onlinebankingweb.wrappers.UserCustomerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        UserDTO userDTO = new UserDTO();
        CustomerDTO customerDTO = new CustomerDTO();
        UserCustomerWrapper userCustomerWrapper = new UserCustomerWrapper(userDTO, customerDTO);
        model.addAttribute("userCustomerWrapper", userCustomerWrapper);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("userCustomerWrapper") UserCustomerWrapper userCustomerWrapper) {
        String password = userCustomerWrapper.getUserDTO().getPassword();
        String passwordHash = passwordEncoder.encode(password);
        userCustomerWrapper.getUserDTO().setPassword(passwordHash);
        authService.attemptRegisterCustomer(userCustomerWrapper.getUserDTO(), userCustomerWrapper.getCustomerDTO());
        return null;
    }
}
