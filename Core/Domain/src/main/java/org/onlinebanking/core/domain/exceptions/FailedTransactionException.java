package org.onlinebanking.core.domain.exceptions;

public class FailedTransactionException extends RuntimeException {
    public FailedTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedTransactionException(String message) {
        super(message);
    }
}
