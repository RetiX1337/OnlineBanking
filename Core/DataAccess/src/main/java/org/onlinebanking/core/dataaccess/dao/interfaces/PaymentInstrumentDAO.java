package org.onlinebanking.core.dataaccess.dao.interfaces;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.Card;

import java.util.List;

public interface PaymentInstrumentDAO extends DAOInterface<PaymentInstrument> {
    List<PaymentInstrument> findByBankAccount(BankAccount bankAccount);
    Card findByCardNumber(String cardNumber);
}
