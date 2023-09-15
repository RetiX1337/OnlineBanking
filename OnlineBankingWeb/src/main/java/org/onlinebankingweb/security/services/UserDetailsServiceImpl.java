package org.onlinebankingweb.security.services;

import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.models.user.UserRole;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
//        User user = new User();
//        user.setEmail("111@gmail.com");
//        user.setUsername("whyretski");
//        user.setPasswordHash(new BCryptPasswordEncoder().encode("1111111"));
//        user.setId(1L);
//        user.setRoles(List.of(UserRole.USER_ROLE, UserRole.GUEST_ROLE, UserRole.ADMIN_ROLE));
        System.out.println(user.getUsername());
        System.out.println(user.getRoles());
        List<UserRole> userRoles = user.getRoles();
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority(userRole.toString()));
        }

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUserId(user.getId());
        userPrincipal.setEmail(user.getEmail());
        userPrincipal.setPasswordHash(user.getPasswordHash());
        userPrincipal.setAuthorities(authorities);

        return userPrincipal;
    }
}
