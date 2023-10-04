package org.onlinebanking.core.businesslogic.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.PaymentInstrumentDAO;
import org.onlinebanking.core.domain.models.paymentinstruments.cards.Card;
import org.onlinebanking.core.domain.exceptions.DAOException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.PaymentInstrumentFactoryException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class PaymentInstrumentServiceImpl implements PaymentInstrumentService {
    private final static String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "The PaymentInstrument couldn't be found by %s";
    private final static Logger logger = LogManager.getLogger(BankAccountServiceImpl.class);
    private final PaymentInstrumentDAO paymentInstrumentDAO;

    @Autowired
    public PaymentInstrumentServiceImpl(PaymentInstrumentDAO paymentInstrumentDAO) {
        this.paymentInstrumentDAO = paymentInstrumentDAO;
    }


    @Transactional
    @Override
    public PaymentInstrument openPaymentInstrument(PaymentInstrument paymentInstrument) {
        if (paymentInstrument instanceof Card) {
            populateCard(paymentInstrument);
        }
        try {
            return paymentInstrumentDAO.save(paymentInstrument);
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
    }

    @Transactional
    @Override
    public PaymentInstrument updatePaymentInstrument(PaymentInstrument paymentInstrument) {
        try {
            return paymentInstrumentDAO.update(paymentInstrument);
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PaymentInstrument> findByBankAccount(BankAccount bankAccount) {
        List<PaymentInstrument> paymentInstruments;
        try {
            paymentInstruments = paymentInstrumentDAO.findByBankAccount(bankAccount);
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
        if (paymentInstruments.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                    "Bank Account " + bankAccount.getAccountNumber()));
        }
        return paymentInstruments;
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentInstrument findById(Long id) {
        try {
            return paymentInstrumentDAO.findById(id);
        } catch (NoResultException e) {
            logger.error(e);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, "ID " + id));
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
    }

    private PaymentInstrument findByCardNumber(String cardNumber) {
        try {
            return paymentInstrumentDAO.findByCardNumber(cardNumber);
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
    }

    private Card populateCard(PaymentInstrument paymentInstrument) {
        String cardNumber;
        Card card = (Card) paymentInstrument;
        card.setExpiryDate(generateExpiryDate());
        do {
            cardNumber = generateCardNumber();
        } while (findByCardNumber(cardNumber) != null);
        card.setCardNumber(cardNumber);
        return card;
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
