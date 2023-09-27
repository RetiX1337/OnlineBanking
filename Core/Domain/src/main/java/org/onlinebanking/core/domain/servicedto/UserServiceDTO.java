package org.onlinebanking.core.domain.servicedto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserServiceDTO {
    @NotBlank(message = "Username can't be blank")
    private String username;
    @NotBlank(message = "Email can't be blank")
    @Email(regexp = ".+[@].+[\\.].+", message = "Please provide correct email address (example@test.com)")
    private String email;
    @NotBlank(message = "Password can't be blank")
    private String password;

    public UserServiceDTO() {

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
