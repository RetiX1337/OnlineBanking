package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import java.util.List;

public interface PaymentInstrumentService {
    PaymentInstrument openPaymentInstrument(PaymentInstrument paymentInstrument);
    PaymentInstrument updatePaymentInstrument(PaymentInstrument paymentInstrument);
    List<PaymentInstrument> findByBankAccount(BankAccount bankAccount);
    PaymentInstrument findById(Long id);

}
