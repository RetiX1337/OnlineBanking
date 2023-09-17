package org.onlinebankingweb.wrappers;

import org.onlinebanking.core.domain.dto.CustomerDTO;
import org.onlinebanking.core.domain.dto.UserDTO;

public class UserCustomerWrapper {
    private UserDTO userDTO;
    private CustomerDTO customerDTO;

    public UserCustomerWrapper(UserDTO userDTO, CustomerDTO customerDTO) {
        this.userDTO = userDTO;
        this.customerDTO = customerDTO;
    }

    public UserCustomerWrapper() {
    }

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
