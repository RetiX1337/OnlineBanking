package org.onlinebanking.core.domain.exceptions;

public class FailedUserRegistrationException extends RuntimeException {
    public FailedUserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedUserRegistrationException(String message) {
        super(message);
    }
}
