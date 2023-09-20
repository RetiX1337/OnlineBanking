package org.onlinebanking.core.domain.dto.requests;

public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;

    public UserRegistrationRequest() {

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
