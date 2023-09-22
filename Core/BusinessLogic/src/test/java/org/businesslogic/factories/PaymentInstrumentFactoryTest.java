package org.businesslogic.factories;

import org.junit.Before;
import org.junit.Test;
import org.onlinebanking.core.businesslogic.factories.PaymentInstrumentFactory;
import org.onlinebanking.core.domain.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebanking.core.domain.dto.requests.paymentinstruments.cards.DebitCardRequest;
import org.onlinebanking.core.domain.dto.requests.paymentinstruments.PaymentInstrumentRequest;
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

    @Test(expected = PaymentInstrumentFactoryException.class)
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isNull_thenExceptionIsThrown() {
        paymentInstrumentFactory.createPaymentInstrument(null, new BankAccount());
    }

    @Test(expected = PaymentInstrumentFactoryException.class)
    public void createPaymentInstrument_whenBankAccount_isNull_thenExceptionIsThrown() {
        paymentInstrumentFactory.createPaymentInstrument(new PaymentInstrumentRequest() {}, null);
    }

    @Test(expected = PaymentInstrumentFactoryException.class)
    public void createPaymentInstrument_whenPaymentInstrumentDTO_isNotInFactory_thenExceptionIsThrown() {
        PaymentInstrumentRequest mockedPaymentInstrumentRequest = new PaymentInstrumentRequest() {
        };
        BankAccount bankAccount = new BankAccount();
        paymentInstrumentFactory.createPaymentInstrument(mockedPaymentInstrumentRequest, bankAccount);
    }
}
