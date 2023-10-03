package org.onlinebankingweb.mappers;

import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponse;
import org.onlinebankingweb.factories.PaymentInstrumentResponseFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentInstrumentMapper {
    private final PaymentInstrumentService paymentInstrumentService;

    public PaymentInstrumentMapper(PaymentInstrumentService paymentInstrumentService) {
        this.paymentInstrumentService = paymentInstrumentService;
    }

    public List<PaymentInstrumentResponse> responseListByBankAccount(BankAccount bankAccount) {
        return paymentInstrumentService.findByBankAccount(bankAccount)
                .stream()
                .map(PaymentInstrumentResponseFactory::createPaymentInstrument)
                .toList();
    }
}
