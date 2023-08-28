package org.onlinebanking.core.domain.dto;

public class UserDTO {
    private String username;
    private String email;
    private String passwordHash;

    public UserDTO() {

    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
