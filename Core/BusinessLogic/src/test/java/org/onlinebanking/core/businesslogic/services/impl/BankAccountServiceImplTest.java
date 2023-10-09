package org.onlinebanking.core.businesslogic.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.BankAccountDAO;
import org.onlinebanking.core.domain.exceptions.DAOException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;

import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class BankAccountServiceImplTest {
    @Mock
    private BankAccountDAO bankAccountDAO;
    @Mock
    private PaymentInstrumentService paymentInstrumentService;
    private BankAccountServiceImpl bankAccountServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bankAccountServiceImpl = new BankAccountServiceImpl(bankAccountDAO, paymentInstrumentService);
    }

    @Test
    public void openBankAccount_whenBankAccount_isValid() {
        BankAccount bankAccount = new BankAccount();
        Customer customer = new Customer();
        bankAccount.setAccountHolder(customer);

        BankAccount expectedSavedBankAccount = new BankAccount();
        expectedSavedBankAccount.setAccountHolder(customer);

        Mockito.when(bankAccountDAO.findByAccountNumber(any())).thenThrow(NoResultException.class);
        Mockito.when(bankAccountDAO.save(any())).thenReturn(expectedSavedBankAccount);

        BankAccount savedBankAccount = bankAccountServiceImpl.openBankAccount(bankAccount);

        assertNotNull(savedBankAccount);
        assertEquals(bankAccount.getAccountHolder(), savedBankAccount.getAccountHolder());

        Mockito.verify(bankAccountDAO, Mockito.times(1)).save(any());
        Mockito.verify(paymentInstrumentService, Mockito.times(1)).openPaymentInstrument(any());
    }

    @Test
    public void openBankAccount_whenBankAccount_isNull() {
        assertThrows(DAOException.class, () -> bankAccountServiceImpl.openBankAccount(null));
    }

    @Test
    public void openBankAccount_whenAccountHolder_isNull() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder(null);
        assertThrows(DAOException.class, () -> bankAccountServiceImpl.openBankAccount(bankAccount));
    }

    @Test
    public void openBankAccount_whenBankAccountDAO_fails() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder(new Customer());

        Mockito.when(bankAccountDAO.findByAccountNumber(any())).thenThrow(RuntimeException.class);
        Mockito.when(bankAccountDAO.save(any())).thenThrow(RuntimeException.class);

        assertThrows(DAOException.class, () -> bankAccountServiceImpl.openBankAccount(bankAccount));
    }

    @Test
    public void openBankAccount_whenPaymentInstrumentService_fails() {
        BankAccount bankAccount = new BankAccount();
        Customer customer = new Customer();
        bankAccount.setAccountHolder(customer);

        BankAccount expectedSavedBankAccount = new BankAccount();
        expectedSavedBankAccount.setAccountHolder(customer);

        Mockito.when(bankAccountDAO.findByAccountNumber(any())).thenThrow(NoResultException.class);
        Mockito.when(bankAccountDAO.save(any())).thenReturn(expectedSavedBankAccount);

        Mockito.when(paymentInstrumentService.openPaymentInstrument(any())).thenThrow(DAOException.class);

        assertThrows(DAOException.class, () -> bankAccountServiceImpl.openBankAccount(bankAccount));
    }

    @Test
    public void activateBankAccount_whenBankAccount_isValid() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.deactivateBankAccount();
        bankAccount.setAccountNumber("some_number");

        BankAccount expectedSavedBankAccount = new BankAccount();
        expectedSavedBankAccount.activateBankAccount();
        bankAccount.setAccountNumber("some_number");

        Mockito.when(bankAccountDAO.update(any())).thenReturn(expectedSavedBankAccount);

        assertTrue(bankAccountServiceImpl.activateBankAccount(bankAccount));
    }

    @Test
    public void activateBankAccount_whenBankAccount_isNotPersisted() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.deactivateBankAccount();
        bankAccount.setAccountNumber("some_number");

        Mockito.when(bankAccountDAO.findByAccountNumber(any())).thenThrow(NoResultException.class);

        assertFalse(bankAccountServiceImpl.activateBankAccount(bankAccount));
    }

    @Test
    public void activateBankAccount_whenBankAccount_isNull() {
        assertFalse(bankAccountServiceImpl.activateBankAccount(null));
    }

    @Test
    public void activateBankAccount_whenBankAccount_isActivated() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.activateBankAccount();

        assertFalse(bankAccountServiceImpl.activateBankAccount(bankAccount));
    }

    @Test
    public void deactivateBankAccount_whenBankAccount_isValid() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.activateBankAccount();
        bankAccount.setAccountNumber("some_number");

        BankAccount expectedSavedBankAccount = new BankAccount();
        expectedSavedBankAccount.deactivateBankAccount();
        bankAccount.setAccountNumber("some_number");

        Mockito.when(bankAccountDAO.update(any())).thenReturn(expectedSavedBankAccount);

        assertTrue(bankAccountServiceImpl.deactivateBankAccount(bankAccount));
    }

    @Test
    public void deactivateBankAccount_whenBankAccount_isNotPersisted() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.deactivateBankAccount();
        bankAccount.setAccountNumber("some_number");

        Mockito.when(bankAccountDAO.findByAccountNumber(any())).thenThrow(NoResultException.class);

        assertFalse(bankAccountServiceImpl.deactivateBankAccount(bankAccount));
    }

    @Test
    public void deactivateBankAccount_whenBankAccount_isNull() {
        assertFalse(bankAccountServiceImpl.deactivateBankAccount(null));
    }

    @Test
    public void deactivateBankAccount_whenBankAccount_isDeactivated() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.deactivateBankAccount();

        assertFalse(bankAccountServiceImpl.deactivateBankAccount(bankAccount));
    }
}
