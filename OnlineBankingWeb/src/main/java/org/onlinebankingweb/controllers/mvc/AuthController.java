package org.onlinebankingweb.controllers.mvc;

import org.onlinebanking.core.domain.servicedto.CustomerServiceDTO;
import org.onlinebanking.core.domain.servicedto.UserServiceDTO;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.FailedCustomerRegistrationException;
import org.onlinebanking.core.domain.exceptions.FailedUserRegistrationException;
import org.onlinebankingweb.dto.requests.CustomerRegistrationRequest;
import org.onlinebankingweb.dto.requests.LoginRequest;
import org.onlinebankingweb.dto.requests.UserRegistrationRequest;
import org.onlinebankingweb.dto.responses.LoginResponse;
import org.onlinebankingweb.mappers.CustomerMapper;
import org.onlinebankingweb.mappers.UserMapper;
import org.onlinebankingweb.security.services.AuthService;
import org.onlinebankingweb.dto.wrappers.UserCustomerWrapper;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Validated
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;

    @Autowired
    public AuthController(AuthService authService, UserMapper userMapper, CustomerMapper customerMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
        this.customerMapper = customerMapper;
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginRequest loginRequest = new LoginRequest();
        model.addAttribute("loginRequest", loginRequest);
        return "auth/login/login-form";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, BindingResult result,
                        Model model, HttpServletResponse httpServletResponse) {
        if (result.hasErrors()) {
            return "auth/login/login-form";
        }

        try {
            LoginResponse loginResponse = authService.attemptLogin(loginRequest.getEmail(), loginRequest.getPassword());
            Cookie cookie = new Cookie("jwt", loginResponse.getToken());
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        } catch (AuthenticationException e) {
            model.addAttribute("authenticationExceptionMessage",
                    "The email or password are invalid");
            return "auth/login/login-form";
        } catch (EntityNotFoundException e) {
            model.addAttribute("entityNotFoundExceptionMessage", e.getMessage());
            return "auth/login/login-form";
        }
        return "redirect:/";
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public RedirectView logout(HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return new RedirectView("/");
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        UserCustomerWrapper userCustomerWrapper = new UserCustomerWrapper(new UserRegistrationRequest(), new CustomerRegistrationRequest());
        model.addAttribute("userCustomerWrapper", userCustomerWrapper);
        return "auth/register/registration-form";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userCustomerWrapper") UserCustomerWrapper userCustomerWrapper,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register/registration-form";
        }
        UserServiceDTO userServiceDTO = userMapper.registrationRequestToServiceDTO(userCustomerWrapper.getUserRegistrationRequest());
        CustomerServiceDTO customerServiceDTO = customerMapper.registrationRequestToServiceDTO(userCustomerWrapper.getCustomerRegistrationRequest());

        try {
            authService.attemptRegister(userServiceDTO, customerServiceDTO);
        } catch (FailedCustomerRegistrationException e) {
            model.addAttribute("customerRegistrationExceptionMessage", e.getMessage());
            return "auth/register/registration-form";
        } catch (FailedUserRegistrationException e) {
            model.addAttribute("userRegistrationExceptionMessage", e.getMessage());
            return "auth/register/registration-form";
        }
        return "redirect:/";
    }
}
