package org.onlinebanking.core.businesslogic.services.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.BankAccountDAO;
import org.onlinebanking.core.domain.exceptions.ServiceException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;

import javax.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class BankAccountServiceImplTest {
    @Mock
    private BankAccountDAO bankAccountDAO;
    @Mock
    private PaymentInstrumentService paymentInstrumentService;
    private BankAccountServiceImpl bankAccountServiceImpl;
    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        bankAccountServiceImpl = new BankAccountServiceImpl(bankAccountDAO, paymentInstrumentService);
    }

    @AfterEach
    public void afterEach() throws Exception {
        mocks.close();
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
    public void openBankAccount_whenBankAccountDAO_fails() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder(new Customer());

        Mockito.when(bankAccountDAO.findByAccountNumber(any())).thenThrow(RuntimeException.class);
        Mockito.when(bankAccountDAO.save(any())).thenThrow(RuntimeException.class);

        assertThrows(ServiceException.class, () -> bankAccountServiceImpl.openBankAccount(bankAccount));
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

        Mockito.when(paymentInstrumentService.openPaymentInstrument(any())).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> bankAccountServiceImpl.openBankAccount(bankAccount));
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
    public void deactivateBankAccount_whenBankAccount_isDeactivated() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.deactivateBankAccount();

        assertFalse(bankAccountServiceImpl.deactivateBankAccount(bankAccount));
    }

    @Test
    public void updateBankAccount_whenBankAccount_isValid() {
        BankAccount bankAccount = new BankAccount();
        Customer customer = new Customer();
        bankAccount.setAccountHolder(customer);

        BankAccount expectedSavedBankAccount = new BankAccount();
        expectedSavedBankAccount.setAccountHolder(customer);

        Mockito.when(bankAccountDAO.update(any())).thenReturn(expectedSavedBankAccount);

        BankAccount updatedBankAccount = bankAccountServiceImpl.updateBankAccount(bankAccount);

        assertNotNull(updatedBankAccount);
        assertEquals(bankAccount.getAccountHolder(), updatedBankAccount.getAccountHolder());

        Mockito.verify(bankAccountDAO, Mockito.times(1)).update(any());
    }

    @Test
    public void updateBankAccount_whenBankAccountDAO_fails() {
        BankAccount bankAccount = new BankAccount();
        Mockito.when(bankAccountDAO.update(any())).thenThrow(RuntimeException.class);

        assertThrows(ServiceException.class, () -> bankAccountServiceImpl.updateBankAccount(bankAccount));
    }

    @Test
    public void findByCustomer_whenCustomer_isValid() {
        Customer customer = new Customer();

        List<BankAccount> expectedBankAccounts = new ArrayList<>();
        expectedBankAccounts.add(new BankAccount());
        expectedBankAccounts.add(new BankAccount());
        expectedBankAccounts.add(new BankAccount());

        Mockito.when(bankAccountDAO.findByCustomer(any())).thenReturn(expectedBankAccounts);

        List<BankAccount> actualBankAccounts = bankAccountServiceImpl.findByCustomer(customer);
        assertEquals(expectedBankAccounts, actualBankAccounts);
    }

    @Test
    public void findByCustomer_whenBankAccountDAO_fails() {
        Mockito.when(bankAccountDAO.findByCustomer(any())).thenThrow(RuntimeException.class);

        assertThrows(ServiceException.class, () -> bankAccountServiceImpl.findByCustomer(new Customer()));
    }

    @Test
    public void findByAccountNumber_whenAccountNumber_isValid() {
        String accountNumber = "some_number";
        BankAccount expectedBankAccount = new BankAccount();
        expectedBankAccount.setAccountNumber(accountNumber);

        Mockito.when(bankAccountDAO.findByAccountNumber(accountNumber)).thenReturn(expectedBankAccount);

        BankAccount actualBankAccount = bankAccountServiceImpl.findByAccountNumber(accountNumber);

        assertEquals(expectedBankAccount, actualBankAccount);
        Mockito.verify(bankAccountDAO, Mockito.times(1)).findByAccountNumber(any());
    }

    @Test
    public void findByAccountNumber_whenNoResult() {
        String accountNumber = "some_number";

        Mockito.when(bankAccountDAO.findByAccountNumber(any())).thenThrow(NoResultException.class);

        assertThrows(EntityNotFoundException.class, () -> bankAccountServiceImpl.findByAccountNumber(accountNumber));
    }

    @Test
    public void findByAccountNumber_whenBankAccountDAO_fails() {
        String accountNumber = "some_number";

        Mockito.when(bankAccountDAO.findByAccountNumber(any())).thenThrow(RuntimeException.class);

        assertThrows(ServiceException.class, () -> bankAccountServiceImpl.findByAccountNumber(accountNumber));
    }
}
