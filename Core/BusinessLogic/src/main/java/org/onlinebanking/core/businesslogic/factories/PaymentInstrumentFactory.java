package org.onlinebanking.core.businesslogic.factories;

import com.google.common.base.Optional;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrumentType;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.function.Supplier;

@Component
public class PaymentInstrumentFactory {
    public final HashMap<PaymentInstrumentType, Supplier<? extends PaymentInstrument>> paymentInstrumentMap;

    public PaymentInstrumentFactory() {
        this.paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMapSetUp();
    }

    private void paymentInstrumentMapSetUp() {
        this.paymentInstrumentMap.put(PaymentInstrumentType.BANK_TRANSFER, BankTransfer::new);
        this.paymentInstrumentMap.put(PaymentInstrumentType.CREDIT_CARD, CreditCard::new);
        this.paymentInstrumentMap.put(PaymentInstrumentType.DEBIT_CARD, DebitCard::new);
    }

    public PaymentInstrument createPaymentInstrument(PaymentInstrumentType paymentInstrumentType) {
        Supplier<? extends PaymentInstrument> supplier = paymentInstrumentMap.get(paymentInstrumentType);
        if (supplier == null) {
            throw new IllegalArgumentException("Invalid Payment Instrument type: " + paymentInstrumentType.name());
        } else {
            return supplier.get();
        }
    }
}
