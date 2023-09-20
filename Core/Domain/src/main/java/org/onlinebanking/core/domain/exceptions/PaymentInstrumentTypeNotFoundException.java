package org.onlinebanking.core.domain.exceptions;

public class PaymentInstrumentTypeNotFoundException extends RuntimeException {
    public PaymentInstrumentTypeNotFoundException(String message) {
        super(message);
    }
}
