package org.onlinebankingweb.dto.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LoginRequest {
    @NotBlank(message = "Email can't be blank")
    @Email(regexp = ".+[@].+[\\.].+", message = "Please provide correct email address (example@test.com)")
    private String email;
    @NotBlank(message = "Password can't be blank")
    private String password;

    public LoginRequest() {

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
