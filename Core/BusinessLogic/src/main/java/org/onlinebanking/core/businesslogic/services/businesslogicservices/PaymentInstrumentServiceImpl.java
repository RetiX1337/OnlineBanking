package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlinebanking.core.businesslogic.factories.PaymentInstrumentFactory;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.PaymentInstrumentDAO;
import org.onlinebanking.core.domain.dto.requests.CardRequest;
import org.onlinebanking.core.domain.dto.requests.PaymentInstrumentRequest;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.EntityNotSavedException;
import org.onlinebanking.core.domain.exceptions.EntityNotUpdatedException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.persistence.PersistenceException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class PaymentInstrumentServiceImpl implements PaymentInstrumentService {
    private final static String ENTITY_NOT_SAVED_EXCEPTION_MESSAGE = "The PaymentInstrument couldn't be saved";
    private final static String ENTITY_NOT_UPDATED_EXCEPTION_MESSAGE = "The PaymentInstrument couldn't be updated";
    private final static String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "The PaymentInstrument couldn't be found by %s";
    private final static String ENTITY_NOT_FOUND_ERROR = "Error finding PaymentInstrument by %s";
    private final static Logger logger = LogManager.getLogger(BankAccountServiceImpl.class);
    private final PaymentInstrumentDAO paymentInstrumentDAO;
    private final PaymentInstrumentFactory paymentInstrumentFactory;

    @Autowired
    public PaymentInstrumentServiceImpl(PaymentInstrumentDAO paymentInstrumentDAO,
                                        PaymentInstrumentFactory paymentInstrumentFactory) {
        this.paymentInstrumentDAO = paymentInstrumentDAO;
        this.paymentInstrumentFactory = paymentInstrumentFactory;
    }


    @Transactional
    @Override
    public PaymentInstrument openPaymentInstrument(PaymentInstrumentRequest paymentInstrumentRequest,
                                                   BankAccount bankAccount) {
        PaymentInstrument paymentInstrument;

        if (paymentInstrumentRequest instanceof CardRequest) {
            paymentInstrumentRequest = initCardDTO(paymentInstrumentRequest);
        }

        paymentInstrument = paymentInstrumentFactory.createPaymentInstrument(paymentInstrumentRequest, bankAccount);
        try {
            paymentInstrumentDAO.save(paymentInstrument);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new EntityNotSavedException(ENTITY_NOT_SAVED_EXCEPTION_MESSAGE, e);
        }

        return paymentInstrument;
    }

    @Transactional
    @Override
    public PaymentInstrument updatePaymentInstrument(PaymentInstrument paymentInstrument) {
        try {
            return paymentInstrumentDAO.update(paymentInstrument);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new EntityNotUpdatedException(ENTITY_NOT_UPDATED_EXCEPTION_MESSAGE, e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PaymentInstrument> findByBankAccount(BankAccount bankAccount) {
        return paymentInstrumentDAO.findByBankAccount(bankAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentInstrument findById(Long id) {
        PaymentInstrument paymentInstrument;
        try {
            paymentInstrument = paymentInstrumentDAO.findById(id);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_ERROR, "ID " + id), e);
        }

        if (paymentInstrument == null) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, "ID " + id));
        }
        return paymentInstrument;
    }

    private CardRequest initCardDTO(PaymentInstrumentRequest paymentInstrumentRequest) {
        String cardNumber;
        CardRequest cardDTO = (CardRequest) paymentInstrumentRequest;
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
        return String.valueOf((leftLimit + (rand.nextLong(rightLimit - leftLimit + 1))));
    }
}
