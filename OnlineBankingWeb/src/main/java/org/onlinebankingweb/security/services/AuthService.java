package org.onlinebankingweb.security.services;

import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.servicedto.CustomerServiceDTO;
import org.onlinebanking.core.domain.servicedto.UserServiceDTO;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.models.user.UserRole;
import org.onlinebankingweb.dto.responses.LoginResponse;
import org.onlinebankingweb.security.services.jwt.JWTService;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthService {
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CustomerService customerService;

    @Autowired
    public AuthService(JWTService jwtService, AuthenticationManager authenticationManager,
                       UserService userService, CustomerService customerService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.customerService = customerService;
    }

    // if registration goes wrong exception is thrown
    @Transactional
    public void attemptRegister(UserServiceDTO userServiceDTO,
                                CustomerServiceDTO customerServiceDTO) {
        User user = userService.registerUser(userServiceDTO);
        Customer customer = customerService.registerCustomer(customerServiceDTO);
        customerService.assignCustomerToUser(customer, user);
    }

    public LoginResponse attemptLogin(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        JWTService.Request request =
                new JWTService.Request(principal.getUserId(), principal.getUsername(),
                        principal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .map(UserRole::valueOf)
                                .toList());

        String token = jwtService.issueToken(request);

        return new LoginResponse(token);
    }
}
