package org.onlinebanking.core.businesslogic.services;

import org.onlinebanking.core.domain.dto.PaymentInstrumentDTO;
import org.onlinebanking.core.domain.dto.TransactionDTO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import java.util.List;

public interface PaymentInstrumentService {
    boolean openPaymentInstrument(PaymentInstrumentDTO paymentInstrumentDTO);
    PaymentInstrument updatePaymentInstrument(PaymentInstrument paymentInstrument);
    List<PaymentInstrument> findByBankAccount(BankAccount bankAccount);
    PaymentInstrument findById(Long id);

}
