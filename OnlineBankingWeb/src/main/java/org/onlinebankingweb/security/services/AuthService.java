package org.onlinebankingweb.security.services;

import org.onlinebanking.core.businesslogic.services.CustomerService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.dto.CustomerDTO;
import org.onlinebanking.core.domain.dto.UserDTO;
import org.onlinebanking.core.domain.models.user.UserRole;
import org.onlinebankingweb.security.models.LoginResponse;
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

    //TODO
    @Transactional
    public boolean attemptRegister(UserDTO userDTO, CustomerDTO customerDTO) {
        /*
        if (userService.registerUser(userDTO)) {
            customerService.registerCustomer(customerDTO);
            Customer customer = customerService.findByTaxPayerId(customerDTO.getTaxPayerId());
            User user = userService.findByEmail(userDTO.getEmail());
            customerService.assignCustomerToUser(customer, user);
        }
         */
        return false;
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
