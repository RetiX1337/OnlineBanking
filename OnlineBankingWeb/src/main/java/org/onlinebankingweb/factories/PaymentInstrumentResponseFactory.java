package org.onlinebankingweb.factories;

import org.onlinebanking.core.domain.exceptions.PaymentInstrumentFactoryException;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;
import org.onlinebankingweb.dto.responses.paymentinstruments.BankTransferResponse;
import org.onlinebankingweb.dto.responses.paymentinstruments.CreditCardResponse;
import org.onlinebankingweb.dto.responses.paymentinstruments.DebitCardResponse;
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.function.Function;

@Component
public class PaymentInstrumentResponseFactory {
    private final static String ARGUMENT_IS_NULL_EXCEPTION_MESSAGE = "Passed argument is null";
    private final static String ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE = "Invalid Payment Instrument Request: %s";
    public static final HashMap<Class<? extends PaymentInstrument>,
            Function<PaymentInstrument, ? extends PaymentInstrumentResponse>> paymentInstrumentMap;

    static {
        paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMapSetUp();
    }

    private static void paymentInstrumentMapSetUp() {
        paymentInstrumentMap.put(BankTransfer.class, (domain)
                -> new BankTransferResponse((BankTransfer) domain));
        paymentInstrumentMap.put(CreditCard.class, (domain)
                -> new CreditCardResponse((CreditCard) domain));
        paymentInstrumentMap.put(DebitCard.class, (domain)
                -> new DebitCardResponse((DebitCard) domain));
    }

    public static PaymentInstrumentResponse createPaymentInstrument(PaymentInstrument paymentInstrument) {
        if (paymentInstrument == null) {
            throw new PaymentInstrumentFactoryException(ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        Function<PaymentInstrument, ? extends PaymentInstrumentResponse> supplierFunction =
                paymentInstrumentMap.get(paymentInstrument.getClass());
        if (supplierFunction == null) {
            throw new PaymentInstrumentFactoryException(String.format(ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE,
                    paymentInstrument.getClass().getSimpleName()));
        } else {
            return supplierFunction.apply(paymentInstrument);
        }
    }
}
