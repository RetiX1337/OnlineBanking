package org.businesslogic.factories;

import org.junit.Before;
import org.junit.Test;
import org.onlinebanking.core.businesslogic.factories.PaymentInstrumentFactory;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.cards.CreditCardServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.cards.DebitCardServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.PaymentInstrumentServiceDTO;
import org.onlinebanking.core.domain.exceptions.PaymentInstrumentFactoryException;
import org.onlinebanking.core.domain.models.BankAccount;
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
        PaymentInstrumentServiceDTO paymentInstrumentServiceDTO = new DebitCardServiceDTO();

        PaymentInstrument paymentInstrument = paymentInstrumentFactory
                .createPaymentInstrument(paymentInstrumentServiceDTO);

        assertEquals(paymentInstrument.getClass(), DebitCard.class);
    }

    @Test
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isCreditCardDTO_thenReturnCreditCard() {
        PaymentInstrumentServiceDTO paymentInstrumentServiceDTO = new CreditCardServiceDTO();

        PaymentInstrument paymentInstrument = paymentInstrumentFactory
                .createPaymentInstrument(paymentInstrumentServiceDTO);

        assertEquals(paymentInstrument.getClass(), CreditCard.class);
    }

    @Test(expected = PaymentInstrumentFactoryException.class)
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isNull_thenExceptionIsThrown() {
        paymentInstrumentFactory.createPaymentInstrument(null);
    }

    @Test(expected = PaymentInstrumentFactoryException.class)
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isNotInFactory_thenExceptionIsThrown() {
        PaymentInstrumentServiceDTO mockedPaymentInstrumentServiceDTO = new PaymentInstrumentServiceDTO() {
        };
        paymentInstrumentFactory.createPaymentInstrument(mockedPaymentInstrumentServiceDTO);
    }
}
