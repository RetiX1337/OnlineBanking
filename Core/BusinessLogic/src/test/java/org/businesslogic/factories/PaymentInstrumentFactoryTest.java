package org.businesslogic.factories;

import org.junit.Before;
import org.junit.Test;
import org.onlinebanking.core.businesslogic.factories.PaymentInstrumentFactory;
import org.onlinebanking.core.domain.dto.requests.CreditCardRequest;
import org.onlinebanking.core.domain.dto.requests.DebitCardRequest;
import org.onlinebanking.core.domain.dto.requests.PaymentInstrumentRequest;
import org.onlinebanking.core.domain.exceptions.PaymentInstrumentTypeNotFoundException;
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
        PaymentInstrumentRequest paymentInstrumentRequest = new DebitCardRequest();
        BankAccount bankAccount = new BankAccount();

        PaymentInstrument paymentInstrument = paymentInstrumentFactory
                .createPaymentInstrument(paymentInstrumentRequest, bankAccount);

        assertEquals(paymentInstrument.getClass(), DebitCard.class);
    }

    @Test
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isCreditCardDTO_thenReturnCreditCard() {
        PaymentInstrumentRequest paymentInstrumentRequest = new CreditCardRequest();
        BankAccount bankAccount = new BankAccount();

        PaymentInstrument paymentInstrument = paymentInstrumentFactory
                .createPaymentInstrument(paymentInstrumentRequest, bankAccount);

        assertEquals(paymentInstrument.getClass(), CreditCard.class);
    }

    @Test(expected = PaymentInstrumentTypeNotFoundException.class)
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isNull_thenExceptionIsThrown() {
        PaymentInstrumentRequest paymentInstrumentRequest = null;
        BankAccount bankAccount = null;
        paymentInstrumentFactory.createPaymentInstrument(paymentInstrumentRequest, bankAccount);
    }

    @Test(expected = PaymentInstrumentTypeNotFoundException.class)
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isNotInFactory_thenExceptionIsThrown() {
        PaymentInstrumentRequest mockedPaymentInstrumentRequest = new PaymentInstrumentRequest() {
        };
        BankAccount bankAccount = new BankAccount();
        paymentInstrumentFactory.createPaymentInstrument(mockedPaymentInstrumentRequest, bankAccount);
    }
}
