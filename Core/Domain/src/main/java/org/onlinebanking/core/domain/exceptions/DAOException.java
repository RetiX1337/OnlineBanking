package org.onlinebanking.core.domain.exceptions;

public class DAOException extends RuntimeException {
    private final static String DAO_EXCEPTION_MESSAGE = "Error while processing operation";

    public DAOException() {
        super(DAO_EXCEPTION_MESSAGE);
    }
}
