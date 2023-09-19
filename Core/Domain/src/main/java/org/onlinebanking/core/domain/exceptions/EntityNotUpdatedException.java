package org.onlinebanking.core.domain.exceptions;

public class EntityNotUpdatedException extends RuntimeException {
    public EntityNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
