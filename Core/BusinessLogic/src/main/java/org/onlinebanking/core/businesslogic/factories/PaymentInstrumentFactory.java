package org.onlinebanking.core.businesslogic.factories;

import org.onlinebanking.core.domain.dto.requests.paymentinstruments.BankTransferRequest;
import org.onlinebanking.core.domain.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebanking.core.domain.dto.requests.paymentinstruments.cards.DebitCardRequest;
import org.onlinebanking.core.domain.dto.requests.paymentinstruments.PaymentInstrumentRequest;
import org.onlinebanking.core.domain.exceptions.PaymentInstrumentFactoryException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.function.BiFunction;

@Component
public class PaymentInstrumentFactory {
    private final static String ARGUMENT_IS_NULL_EXCEPTION_MESSAGE = "Passed argument is null";
    private final static String ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE = "Invalid Payment Instrument Request: %s";
    public final HashMap<Class<? extends PaymentInstrumentRequest>,
            BiFunction<PaymentInstrumentRequest, BankAccount, ? extends PaymentInstrument>> paymentInstrumentMap;

    public PaymentInstrumentFactory() {
        this.paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMapSetUp();
    }

    private void paymentInstrumentMapSetUp() {
        this.paymentInstrumentMap.put(BankTransferRequest.class, (request, bankAccount)
                -> new BankTransfer((BankTransferRequest) request, bankAccount));
        this.paymentInstrumentMap.put(CreditCardRequest.class, (request, bankAccount)
                -> new CreditCard((CreditCardRequest) request, bankAccount));
        this.paymentInstrumentMap.put(DebitCardRequest.class, (request, bankAccount)
                -> new DebitCard((DebitCardRequest) request, bankAccount));
    }

    public PaymentInstrument createPaymentInstrument(PaymentInstrumentRequest paymentInstrumentRequest,
                                                     BankAccount bankAccount) {
        if (paymentInstrumentRequest == null || bankAccount == null) {
            throw new PaymentInstrumentFactoryException(ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        BiFunction<PaymentInstrumentRequest, BankAccount, ? extends PaymentInstrument> supplierFunction =
                paymentInstrumentMap.get(paymentInstrumentRequest.getClass());
        if (supplierFunction == null) {
            throw new PaymentInstrumentFactoryException(String.format(ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE,
                    paymentInstrumentRequest.getClass().getSimpleName()));
        } else {
            return supplierFunction.apply(paymentInstrumentRequest, bankAccount);
        }
    }
}
