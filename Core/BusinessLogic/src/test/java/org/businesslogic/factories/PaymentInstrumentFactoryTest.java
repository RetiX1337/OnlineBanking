package org.businesslogic.factories;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onlinebanking.core.businesslogic.factories.PaymentInstrumentFactory;
import org.onlinebanking.core.domain.dto.CreditCardDTO;
import org.onlinebanking.core.domain.dto.DebitCardDTO;
import org.onlinebanking.core.domain.dto.PaymentInstrumentDTO;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;

import static junit.framework.Assert.*;
public class PaymentInstrumentFactoryTest {
    private PaymentInstrumentFactory paymentInstrumentFactory;

    @Before
    public void setUp() {
        paymentInstrumentFactory = new PaymentInstrumentFactory();
    }

    @Test
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isDebitCardDTO_thenReturnDebitCard() {
        PaymentInstrumentDTO paymentInstrumentDTO = new DebitCardDTO();

        PaymentInstrument paymentInstrument = paymentInstrumentFactory.createPaymentInstrument(paymentInstrumentDTO);

        assertEquals(paymentInstrument.getClass(), DebitCard.class);
    }

    @Test
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isCreditCardDTO_thenReturnCreditCard() {
        PaymentInstrumentDTO paymentInstrumentDTO = new CreditCardDTO();

        PaymentInstrument paymentInstrument = paymentInstrumentFactory.createPaymentInstrument(paymentInstrumentDTO);

        assertEquals(paymentInstrument.getClass(), CreditCard.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isNull_thenExceptionIsThrown() {
        PaymentInstrumentDTO paymentInstrumentDTO = null;
        paymentInstrumentFactory.createPaymentInstrument(paymentInstrumentDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isNotInFactory_thenExceptionIsThrown() {
        PaymentInstrumentDTO mockedPaymentInstrumentDTO = new PaymentInstrumentDTO() {
        };
        paymentInstrumentFactory.createPaymentInstrument(mockedPaymentInstrumentDTO);
    }
}
