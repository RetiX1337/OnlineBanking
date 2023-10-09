package org.onlinebanking.core.businesslogic.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iban4j.Iban;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.BankAccountDAO;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.exceptions.DAOException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final static String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "The BankAccount couldn't be found by %s";
    private final static Logger logger = LogManager.getLogger(BankAccountServiceImpl.class);
    private final BankAccountDAO bankAccountDAO;
    private final PaymentInstrumentService paymentInstrumentService;

    @Autowired
    public BankAccountServiceImpl(BankAccountDAO bankAccountDAO, PaymentInstrumentService paymentInstrumentService) {
        this.bankAccountDAO = bankAccountDAO;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    @Transactional
    @Override
    public BankAccount openBankAccount(BankAccount bankAccount) {
        if (bankAccount == null || bankAccount.getAccountHolder() == null) {
            throw new DAOException();
        }

        populateBankAccount(bankAccount);
        BankAccount savedBankAccount;
        try {
            savedBankAccount = bankAccountDAO.save(bankAccount);
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
        paymentInstrumentService.openPaymentInstrument(initBankTransfer(savedBankAccount));
        return savedBankAccount;
    }

    @Transactional
    @Override
    public boolean activateBankAccount(BankAccount bankAccount) {
        if (bankAccount == null || bankAccount.isActive() || !isPresent(bankAccount)) {
            return false;
        }
        bankAccount.activateBankAccount();
        updateBankAccount(bankAccount);
        return true;
    }

    @Transactional
    @Override
    public boolean deactivateBankAccount(BankAccount bankAccount) {
        if (bankAccount == null || !bankAccount.isActive() || !isPresent(bankAccount) ) {
            return false;
        }
        bankAccount.deactivateBankAccount();
        updateBankAccount(bankAccount);
        return true;
    }

    @Transactional
    @Override
    public BankAccount updateBankAccount(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new DAOException();
        }

        try {
            return bankAccountDAO.update(bankAccount);
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BankAccount> findByCustomer(Customer customer) {
        if (customer == null) {
            throw new DAOException();
        }

        List<BankAccount> accounts;
        try {
            accounts = bankAccountDAO.findByCustomer(customer);
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
        return accounts;
    }

    @Transactional(readOnly = true)
    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new DAOException();
        }

        try {
            return bankAccountDAO.findByAccountNumber(accountNumber);
        } catch (NoResultException e) {
            logger.error(e);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                    "Bank Account " + accountNumber));
        } catch (Exception e) {
            logger.error(e);
            throw new DAOException();
        }
    }

    private String getUniqueIban() {
        String iban;
        while (true) {
            iban = Iban.random().getAccountNumber();
            try {
                findByAccountNumber(iban);
            } catch (EntityNotFoundException ignored) {
                return iban;
            }
        }
    }

    private boolean isPresent(BankAccount bankAccount) {
        try {
            findByAccountNumber(bankAccount.getAccountNumber());
            return true;
        } catch (EntityNotFoundException ignored) {
            return false;
        }
    }

    private void populateBankAccount(BankAccount bankAccount) {
        String iban = getUniqueIban();
        bankAccount.setAccountBalance(BigDecimal.ZERO);
        bankAccount.setAccountNumber(iban);
        bankAccount.activateBankAccount();
    }

    private BankTransfer initBankTransfer(BankAccount bankAccount) {
        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setBankAccount(bankAccount);
        return bankTransfer;
    }
}
