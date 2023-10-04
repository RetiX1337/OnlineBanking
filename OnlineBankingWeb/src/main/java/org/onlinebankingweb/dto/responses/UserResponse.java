package org.onlinebankingweb.dto.responses;

import org.onlinebanking.core.domain.models.user.User;
import org.onlinebanking.core.domain.models.user.UserRole;

import java.util.List;
import java.util.Objects;

public class UserResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final List<UserRole> roles;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<UserRole> getRoles() {
        return roles;
    }
}
