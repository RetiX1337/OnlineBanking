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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, roles);
    }
}
