package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.dataaccess.dao.interfaces.PaymentInstrumentDAO;
import org.onlinebanking.core.domain.dto.PaymentInstrumentDTO;
import org.onlinebanking.core.domain.dto.TransactionDTO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class PaymentInstrumentServiceImpl implements PaymentInstrumentService {
    private final PaymentInstrumentDAO paymentInstrumentDAO;
    private final TransactionService transactionService;

    public PaymentInstrumentServiceImpl(@Autowired PaymentInstrumentDAO paymentInstrumentDAO,
                                        @Autowired TransactionService transactionService) {
        this.paymentInstrumentDAO = paymentInstrumentDAO;
        this.transactionService = transactionService;
    }


    @Override
    public boolean openPaymentInstrument(PaymentInstrumentDTO paymentInstrumentDTO) {
        return false;
    }

    @Override
    public boolean closePaymentInstrument(PaymentInstrument paymentInstrument) {
        return false;
    }

    @Override
    public PaymentInstrument updatePaymentInstrument(PaymentInstrument paymentInstrument) {
        return null;
    }

    @Override
    public List<PaymentInstrument> findByBankAccount(BankAccount bankAccount) {
        return null;
    }
}
