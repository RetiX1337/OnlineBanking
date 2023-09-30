package org.onlinebankingweb.converters;

import org.onlinebankingweb.dto.requests.paymentinstruments.BankTransferRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.PaymentInstrumentRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.DebitCardRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PaymentInstrumentRequestConverter implements Converter<String, PaymentInstrumentRequest> {
    @Override
    public PaymentInstrumentRequest convert(String paymentInstrumentRequestType) {
        return switch (paymentInstrumentRequestType) {
            case "BANK_TRANSFER": yield new BankTransferRequest();
            case "DEBIT_CARD": yield new DebitCardRequest();
            case "CREDIT_CARD": yield new CreditCardRequest();
            default: throw new IllegalArgumentException();
        };
    }
}
