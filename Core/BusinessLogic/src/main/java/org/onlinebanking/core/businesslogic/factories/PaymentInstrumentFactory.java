package org.onlinebanking.core.businesslogic.factories;

import org.onlinebanking.core.domain.dto.BankTransferDTO;
import org.onlinebanking.core.domain.dto.CreditCardDTO;
import org.onlinebanking.core.domain.dto.DebitCardDTO;
import org.onlinebanking.core.domain.dto.PaymentInstrumentDTO;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.function.Function;

@Component
public class PaymentInstrumentFactory {
    public final HashMap<Class<? extends PaymentInstrumentDTO>,
            Function<PaymentInstrumentDTO, ? extends PaymentInstrument>> paymentInstrumentMap;

    public PaymentInstrumentFactory() {
        this.paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMapSetUp();
    }

    private void paymentInstrumentMapSetUp() {
        this.paymentInstrumentMap.put(BankTransferDTO.class, (DTO) -> new BankTransfer((BankTransferDTO) DTO));
        this.paymentInstrumentMap.put(CreditCardDTO.class, (DTO) -> new CreditCard((CreditCardDTO) DTO));
        this.paymentInstrumentMap.put(DebitCardDTO.class, (DTO) -> new DebitCard((DebitCardDTO) DTO));
    }

    public PaymentInstrument createPaymentInstrument(PaymentInstrumentDTO paymentInstrumentDTO) {
        if (paymentInstrumentDTO == null) {
            throw new IllegalArgumentException("Passed argument is null");
        }

        Function<PaymentInstrumentDTO, ? extends PaymentInstrument> supplierFunction = paymentInstrumentMap.get(paymentInstrumentDTO.getClass());
        if (supplierFunction == null) {
            throw new IllegalArgumentException("Invalid Payment Instrument DTO: " + paymentInstrumentDTO.getClass().getSimpleName());
        } else {
            return supplierFunction.apply(paymentInstrumentDTO);
        }

    }
}
