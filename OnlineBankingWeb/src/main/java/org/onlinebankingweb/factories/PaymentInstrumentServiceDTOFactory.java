package org.onlinebankingweb.factories;

import org.onlinebanking.core.domain.exceptions.PaymentInstrumentFactoryException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.BankTransferServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.PaymentInstrumentServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.cards.CreditCardServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.cards.DebitCardServiceDTO;
import org.onlinebankingweb.dto.requests.paymentinstruments.BankTransferRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.PaymentInstrumentRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.DebitCardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PaymentInstrumentServiceDTOFactory {
    private final static String ARGUMENT_IS_NULL_EXCEPTION_MESSAGE = "Passed argument is null";
    private final static String ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE = "Invalid Payment Instrument Request: %s";
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PaymentInstrumentServiceDTOFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public  PaymentInstrumentServiceDTO createPaymentInstrument(PaymentInstrumentRequest paymentInstrumentRequest,
                                                               BankAccount bankAccount) {
        if (paymentInstrumentRequest == null) {
            throw new PaymentInstrumentFactoryException(ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        String className = paymentInstrumentRequest.getClass().getSimpleName();
        return switch (className) {
            case "BankTransferRequest": yield initBankTransferServiceDTO((BankTransferRequest)
                    paymentInstrumentRequest, bankAccount);
            case "CreditCardRequest": yield initCreditCardServiceDTO((CreditCardRequest)
                    paymentInstrumentRequest, bankAccount);
            case "DebitCardRequest": yield initDebitCardServiceDTO((DebitCardRequest)
                    paymentInstrumentRequest, bankAccount);
            default: throw new PaymentInstrumentFactoryException(ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE);
        };
    }

    private BankTransferServiceDTO initBankTransferServiceDTO(BankTransferRequest bankTransferRequest,
                                                              BankAccount bankAccount) {
        BankTransferServiceDTO bankTransferServiceDTO = new BankTransferServiceDTO();
        bankTransferServiceDTO.setBankAccount(bankAccount);
        return bankTransferServiceDTO;
    }

    private CreditCardServiceDTO initCreditCardServiceDTO(CreditCardRequest creditCardRequest,
                                                            BankAccount bankAccount) {
        CreditCardServiceDTO creditCardServiceDTO = new CreditCardServiceDTO();
        creditCardServiceDTO.setBankAccount(bankAccount);
        creditCardServiceDTO.setCVVHash(passwordEncoder.encode(creditCardRequest.getCVV()));
        creditCardServiceDTO.setPINHash(passwordEncoder.encode(creditCardRequest.getPIN()));
        creditCardServiceDTO.setCreditLimit(creditCardRequest.getCreditLimit());
        return creditCardServiceDTO;
    }

    private DebitCardServiceDTO initDebitCardServiceDTO(DebitCardRequest debitCardRequest,
                                                           BankAccount bankAccount) {
        DebitCardServiceDTO debitCardServiceDTO = new DebitCardServiceDTO();
        debitCardServiceDTO.setBankAccount(bankAccount);
        debitCardServiceDTO.setCVVHash(passwordEncoder.encode(debitCardRequest.getCVV()));
        debitCardServiceDTO.setPINHash(passwordEncoder.encode(debitCardRequest.getPIN()));
        debitCardServiceDTO.setDailyTransactionLimit(debitCardRequest.getDailyTransactionLimit());
        debitCardServiceDTO.setDailyWithdrawalLimit(debitCardRequest.getDailyWithdrawalLimit());
        return debitCardServiceDTO;
    }
}
