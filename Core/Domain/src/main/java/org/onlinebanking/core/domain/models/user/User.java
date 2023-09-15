package org.onlinebanking.core.domain.models.user;

import org.onlinebanking.core.domain.models.Identifiable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private List<UserRole> roles;

    public User() {

    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }
}
