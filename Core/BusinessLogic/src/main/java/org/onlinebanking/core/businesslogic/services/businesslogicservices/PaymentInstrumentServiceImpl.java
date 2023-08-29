package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.onlinebanking.core.businesslogic.factories.PaymentInstrumentFactory;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.TransactionService;
import org.onlinebanking.core.dataaccess.dao.interfaces.PaymentInstrumentDAO;
import org.onlinebanking.core.domain.dto.CardDTO;
import org.onlinebanking.core.domain.dto.CreditCardDTO;
import org.onlinebanking.core.domain.dto.DebitCardDTO;
import org.onlinebanking.core.domain.dto.PaymentInstrumentDTO;
import org.onlinebanking.core.domain.dto.TransactionDTO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class PaymentInstrumentServiceImpl implements PaymentInstrumentService {
    private final PaymentInstrumentDAO paymentInstrumentDAO;
    private final PaymentInstrumentFactory paymentInstrumentFactory;

    public PaymentInstrumentServiceImpl(@Autowired PaymentInstrumentDAO paymentInstrumentDAO,
                                        @Autowired PaymentInstrumentFactory paymentInstrumentFactory) {
        this.paymentInstrumentDAO = paymentInstrumentDAO;
        this.paymentInstrumentFactory = paymentInstrumentFactory;
    }


    @Transactional
    @Override
    public boolean openPaymentInstrument(PaymentInstrumentDTO paymentInstrumentDTO) {
        PaymentInstrument paymentInstrument;

        if (paymentInstrumentDTO instanceof CardDTO) {
            paymentInstrumentDTO = initCardDTO(paymentInstrumentDTO);
        }

        try {
            paymentInstrument = paymentInstrumentFactory.createPaymentInstrument(paymentInstrumentDTO);
        } catch (IllegalArgumentException e) {
            return false;
        }

        paymentInstrumentDAO.save(paymentInstrument);

        return true;
    }

    @Transactional
    @Override
    public boolean closePaymentInstrument(PaymentInstrument paymentInstrument) {
        return false;
    }

    @Transactional
    @Override
    public PaymentInstrument updatePaymentInstrument(PaymentInstrument paymentInstrument) {
        return paymentInstrumentDAO.update(paymentInstrument);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PaymentInstrument> findByBankAccount(BankAccount bankAccount) {
        return paymentInstrumentDAO.findByBankAccount(bankAccount);
    }

    private CardDTO initCardDTO(PaymentInstrumentDTO paymentInstrumentDTO) {
        String cardNumber = "";
        CardDTO cardDTO = (CardDTO) paymentInstrumentDTO;
        cardDTO.setExpiryDate(generateExpiryDate());
        do {
            cardNumber = generateCardNumber();
        } while (paymentInstrumentDAO.findByCardNumber(cardNumber) != null);
        cardDTO.setCardNumber(cardNumber);
        return cardDTO;
    }

    private Date generateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 4);
        return calendar.getTime();
    }

    private String generateCardNumber() {
        Random rand = new Random();
        long leftLimit = 1000000000000000L;
        long rightLimit = 9999999999999999L;
        String s = String.valueOf((leftLimit + (long) (rand.nextDouble() * (rightLimit - leftLimit))));
        System.out.println(s);
        return s;
    }
}
