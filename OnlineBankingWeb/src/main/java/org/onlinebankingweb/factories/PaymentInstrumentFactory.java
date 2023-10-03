package org.onlinebankingweb.factories;

import org.onlinebanking.core.domain.exceptions.PaymentInstrumentFactoryException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.CreditCard;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;
import org.onlinebankingweb.dto.requests.paymentinstruments.BankTransferRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.PaymentInstrumentRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.DebitCardRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PaymentInstrumentFactory {
    private final static String ARGUMENT_IS_NULL_EXCEPTION_MESSAGE = "Passed argument is null";
    private final static String ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE = "Invalid Payment Instrument Request: %s";
    private final PasswordEncoder passwordEncoder;

    public PaymentInstrumentFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public PaymentInstrument createPaymentInstrument(PaymentInstrumentRequest paymentInstrumentRequest,
                                                                BankAccount bankAccount) {
        if (paymentInstrumentRequest == null) {
            throw new PaymentInstrumentFactoryException(ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        String className = paymentInstrumentRequest.getClass().getSimpleName();
        return switch (className) {
            case "BankTransferRequest": yield initBankTransfer((BankTransferRequest) paymentInstrumentRequest, bankAccount);
            case "CreditCardRequest": yield initCreditCard((CreditCardRequest) paymentInstrumentRequest, bankAccount);
            case "DebitCardRequest": yield initDebitCard((DebitCardRequest) paymentInstrumentRequest, bankAccount);
            default: throw new PaymentInstrumentFactoryException(ARGUMENT_IS_INVALID_EXCEPTION_MESSAGE);
        };
    }

    private BankTransfer initBankTransfer(BankTransferRequest bankTransferRequest, BankAccount bankAccount) {
        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setBankAccount(bankAccount);
        return bankTransfer;
    }

    private CreditCard initCreditCard(CreditCardRequest creditCardRequest, BankAccount bankAccount) {
        CreditCard creditCard = new CreditCard();
        creditCard.setBankAccount(bankAccount);
        creditCard.setCVVHash(passwordEncoder.encode(creditCardRequest.getCVV()));
        creditCard.setPINHash(passwordEncoder.encode(creditCardRequest.getPIN()));
        creditCard.setCreditLimit(creditCardRequest.getCreditLimit());
        return creditCard;
    }

    private DebitCard initDebitCard(DebitCardRequest debitCardRequest, BankAccount bankAccount) {
        DebitCard debitCard = new DebitCard();
        debitCard.setBankAccount(bankAccount);
        debitCard.setCVVHash(passwordEncoder.encode(debitCardRequest.getCVV()));
        debitCard.setPINHash(passwordEncoder.encode(debitCardRequest.getPIN()));
        debitCard.setDailyTransactionLimit(debitCardRequest.getDailyTransactionLimit());
        debitCard.setDailyWithdrawalLimit(debitCardRequest.getDailyWithdrawalLimit());
        return debitCard;
    }
}
