package org.onlinebanking.core.businesslogic.factories;

import org.onlinebanking.core.domain.servicedto.paymentinstruments.BankTransferServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.cards.CreditCardServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.cards.DebitCardServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.PaymentInstrumentServiceDTO;
import org.onlinebanking.core.domain.exceptions.PaymentInstrumentFactoryException;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.function.Function;

@Component
public class PaymentInstrumentFactory {
    private final static String ARGUMENT_IS_NULL_EXCEPTION_MESSAGE = "Passed argument is null";
    private final static String ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE = "Invalid Payment Instrument Request: %s";
    public final HashMap<Class<? extends PaymentInstrumentServiceDTO>,
            Function<PaymentInstrumentServiceDTO, ? extends PaymentInstrument>> paymentInstrumentMap;

    public PaymentInstrumentFactory() {
        this.paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMapSetUp();
    }

    public PaymentInstrument createPaymentInstrument(PaymentInstrumentServiceDTO paymentInstrumentServiceDTO) {
        if (paymentInstrumentServiceDTO == null) {
            throw new PaymentInstrumentFactoryException(ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        Function<PaymentInstrumentServiceDTO, ? extends PaymentInstrument> supplierFunction =
                paymentInstrumentMap.get(paymentInstrumentServiceDTO.getClass());
        if (supplierFunction == null) {
            throw new PaymentInstrumentFactoryException(String.format(ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE,
                    paymentInstrumentServiceDTO.getClass().getSimpleName()));
        } else {
            return supplierFunction.apply(paymentInstrumentServiceDTO);
        }
    }

    private void paymentInstrumentMapSetUp() {
        this.paymentInstrumentMap.put(BankTransferServiceDTO.class, (request)
                -> new BankTransfer((BankTransferServiceDTO) request));
        this.paymentInstrumentMap.put(CreditCardServiceDTO.class, (request)
                -> new CreditCard((CreditCardServiceDTO) request));
        this.paymentInstrumentMap.put(DebitCardServiceDTO.class, (request)
                -> new DebitCard((DebitCardServiceDTO) request));
    }
}
