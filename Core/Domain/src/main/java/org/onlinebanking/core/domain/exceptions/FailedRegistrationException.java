package org.onlinebanking.core.domain.exceptions;

public class FailedRegistrationException extends RuntimeException {
    public FailedRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedRegistrationException(String message) {
        super(message);
    }
}
