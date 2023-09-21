package org.onlinebanking.core.domain.exceptions;

public class FailedCustomerRegistrationException extends RuntimeException {
    public FailedCustomerRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
    public FailedCustomerRegistrationException(String message) {
        super(message);
    }
}
