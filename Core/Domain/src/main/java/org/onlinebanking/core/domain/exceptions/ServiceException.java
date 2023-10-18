package org.onlinebanking.core.domain.exceptions;

public class ServiceException extends RuntimeException {
    private final static String DAO_EXCEPTION_MESSAGE = "Error while processing operation";

    public ServiceException() {
        super(DAO_EXCEPTION_MESSAGE);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
