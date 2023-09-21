package org.onlinebanking.core.domain.exceptions;

public class PaymentInstrumentFactoryException extends RuntimeException {
    public PaymentInstrumentFactoryException(String message) {
        super(message);
    }
}
