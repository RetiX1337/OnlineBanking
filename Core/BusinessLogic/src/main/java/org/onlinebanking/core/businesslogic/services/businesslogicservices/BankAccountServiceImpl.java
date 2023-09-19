package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.iban4j.Iban;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.BankAccountDAO;
import org.onlinebanking.core.domain.dto.BankTransferDTO;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.EntityNotSavedException;
import org.onlinebanking.core.domain.exceptions.EntityNotUpdatedException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final static Logger logger = LogManager.getLogger(BankAccountServiceImpl.class);
    private final static String ENTITY_NOT_SAVED_EXCEPTION_MESSAGE = "The BankAccount couldn't be saved";
    private final static String ENTITY_NOT_UPDATED_EXCEPTION_MESSAGE = "The BankAccount couldn't be updated";
    private final static String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "The BankAccount couldn't be found by %s";
    private final static String ENTITY_NOT_FOUND_ERROR = "Error finding BankAccounts for the given Customer %s";
    private final BankAccountDAO bankAccountDAO;
    private final PaymentInstrumentService paymentInstrumentService;

    @Autowired
    public BankAccountServiceImpl(BankAccountDAO bankAccountDAO, PaymentInstrumentService paymentInstrumentService) {
        this.bankAccountDAO = bankAccountDAO;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    @Transactional
    @Override
    public BankAccount openBankAccount(Customer customer) {
        BankAccount bankAccount = initBankAccount(customer);
        BankAccount savedBankAccount;
        try {
            savedBankAccount = bankAccountDAO.save(bankAccount);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new EntityNotSavedException(ENTITY_NOT_SAVED_EXCEPTION_MESSAGE, e);
        }
        paymentInstrumentService.openPaymentInstrument(initBankTransfer(savedBankAccount));
        return savedBankAccount;
    }

    @Transactional
    @Override
    public boolean activateBankAccount(BankAccount bankAccount) {
        if (!isPresent(bankAccount) || bankAccount.isActive()) {
            return false;
        }

        bankAccount.activateBankAccount();
        updateBankAccount(bankAccount);
        return true;
    }

    @Transactional
    @Override
    public boolean deactivateBankAccount(BankAccount bankAccount) {
        if (!isPresent(bankAccount) || !bankAccount.isActive()) {
            return false;
        }

        bankAccount.deactivateBankAccount();
        updateBankAccount(bankAccount);
        return true;
    }

    @Transactional
    @Override
    public BankAccount updateBankAccount(BankAccount bankAccount) {
        try {
            return bankAccountDAO.update(bankAccount);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new EntityNotUpdatedException(ENTITY_NOT_UPDATED_EXCEPTION_MESSAGE, e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BankAccount> findByCustomer(Customer customer) {
        List<BankAccount> accounts;
        try {
            accounts = bankAccountDAO.findByCustomer(customer);
        } catch (HibernateException e) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR, e);
        }
        if (accounts.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                    customer.getTaxPayerId()));
        }
        return accounts;
    }

    @Transactional(readOnly = true)
    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        try {
            return bankAccountDAO.findByAccountNumber(accountNumber);
        } catch (PersistenceException e) {
            logger.error(e);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                    "Bank Account " + accountNumber));
        }
    }

    private String getUniqueIban() {
        boolean isUnique = false;
        String iban;
        do {
            iban = Iban.random().getAccountNumber();
            if (findByAccountNumber(iban) == null) {
                isUnique = true;
            }
        } while (!isUnique);
        return iban;
    }

    private boolean isPresent(BankAccount bankAccount) {
        return findByAccountNumber(bankAccount.getAccountNumber()) != null;
    }

    private BankTransferDTO initBankTransfer(BankAccount bankAccount) {
        BankTransferDTO bankTransferDTO = new BankTransferDTO();
        bankTransferDTO.setBankAccount(bankAccount);
        return bankTransferDTO;
    }

    private BankAccount initBankAccount(Customer customer) {
        String iban = getUniqueIban();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountBalance(BigDecimal.ZERO);
        bankAccount.setAccountNumber(iban);
        bankAccount.setAccountHolder(customer);
        bankAccount.activateBankAccount();
        return bankAccount;
    }
}
