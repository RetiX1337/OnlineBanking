package org.onlinebanking.core.businesslogic.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlinebanking.core.businesslogic.factories.PaymentInstrumentFactory;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.PaymentInstrumentDAO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.cards.CardServiceDTO;
import org.onlinebanking.core.domain.servicedto.paymentinstruments.PaymentInstrumentServiceDTO;
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
    private final PaymentInstrumentFactory paymentInstrumentFactory;

    @Autowired
    public PaymentInstrumentServiceImpl(PaymentInstrumentDAO paymentInstrumentDAO,
                                        PaymentInstrumentFactory paymentInstrumentFactory) {
        this.paymentInstrumentDAO = paymentInstrumentDAO;
        this.paymentInstrumentFactory = paymentInstrumentFactory;
    }


    @Transactional
    @Override
    public PaymentInstrument openPaymentInstrument(PaymentInstrumentServiceDTO paymentInstrumentServiceDTO) {
        PaymentInstrument paymentInstrument;

        if (paymentInstrumentServiceDTO instanceof CardServiceDTO) {
            paymentInstrumentServiceDTO = initCardDTO(paymentInstrumentServiceDTO);
        }

        try {
            paymentInstrument = paymentInstrumentFactory.createPaymentInstrument(paymentInstrumentServiceDTO);
        } catch (PaymentInstrumentFactoryException e) {
            logger.error(e);
            throw new DAOException();
        }

        try {
            paymentInstrumentDAO.save(paymentInstrument);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new DAOException();
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
            throw new DAOException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PaymentInstrument> findByBankAccount(BankAccount bankAccount) {
        List<PaymentInstrument> paymentInstruments;
        try {
            paymentInstruments = paymentInstrumentDAO.findByBankAccount(bankAccount);
        } catch (PersistenceException e) {
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
        } catch (PersistenceException e) {
            logger.error(e);
            throw new DAOException();
        }
    }

    private PaymentInstrument findByCardNumber(String cardNumber) {
        try {
            return paymentInstrumentDAO.findByCardNumber(cardNumber);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new DAOException();
        }
    }

    private CardServiceDTO initCardDTO(PaymentInstrumentServiceDTO paymentInstrumentServiceDTO) {
        String cardNumber;
        CardServiceDTO cardDTO = (CardServiceDTO) paymentInstrumentServiceDTO;
        cardDTO.setExpiryDate(generateExpiryDate());
        do {
            cardNumber = generateCardNumber();
        } while (findByCardNumber(cardNumber) != null);
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
