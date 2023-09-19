package org.onlinebanking.core.domain.exceptions;

public class EntityNotSavedException extends RuntimeException {
    public EntityNotSavedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotSavedException(String message) {
        super(message);
    }
}
