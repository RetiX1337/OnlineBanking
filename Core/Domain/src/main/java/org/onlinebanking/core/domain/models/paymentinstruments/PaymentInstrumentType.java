package org.onlinebanking.core.domain.models.paymentinstruments;

public enum PaymentInstrumentType {
    BANK_TRANSFER("Bank Transfer"), DEBIT_CARD("Debit Card"), CREDIT_CARD("Credit Card");
    private final String name;

    PaymentInstrumentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
