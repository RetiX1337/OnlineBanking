package org.onlinebanking.core.businesslogic.services.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.exceptions.DAOException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.models.Customer;
import org.onlinebanking.core.domain.models.paymentinstruments.BankTransfer;
import org.onlinebanking.core.domain.models.transactions.Transaction;
import org.onlinebanking.core.domain.exceptions.FailedTransactionException;
import org.onlinebanking.core.domain.models.BankAccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


public class TransactionServiceImplTest {
    @Mock
    private TransactionDAO transactionDAO;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private PaymentInstrumentService paymentInstrumentService;
    private TransactionServiceImpl transactionService;
    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionDAO, bankAccountService, paymentInstrumentService);
    }

    @AfterEach
    public void afterEach() throws Exception {
        mocks.close();
    }

    @Test
    public void processPayment_whenTransaction_isValid() {
        Customer senderCustomer = initValidSenderCustomer();
        Customer receiverCustomer = initValidReceiverCustomer();

        BankAccount sender = initValidBankAccount(senderCustomer, BigDecimal.valueOf(500));
        BankAccount receiver = initValidBankAccount(receiverCustomer, BigDecimal.ZERO);

        BankTransfer bankTransfer = initValidBankTransfer(sender);

        Transaction transaction = initValidTransaction(sender, receiver, bankTransfer);

        transactionService.processPayment(transaction);

        assertEquals(sender.getAccountBalance().compareTo(BigDecimal.valueOf(400)), 0);
        assertEquals(receiver.getAccountBalance().compareTo(BigDecimal.valueOf(100)), 0);

        Mockito.verify(bankAccountService).updateBankAccount(sender);
        Mockito.verify(bankAccountService).updateBankAccount(receiver);
        Mockito.verify(paymentInstrumentService).updatePaymentInstrument(bankTransfer);
        Mockito.verify(transactionDAO).save(transaction);
    }

    @Test
    public void processPayment_whenSenderIsDeactivated_thenThrows_FailedTransactionException() {
        BankAccount sender = initValidBankAccount(initValidSenderCustomer(), BigDecimal.valueOf(500));
        BankAccount receiver = initValidBankAccount(initValidReceiverCustomer(), BigDecimal.ZERO);
        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setBankAccount(sender);
        sender.deactivateBankAccount();
        receiver.activateBankAccount();
        Transaction transaction = initValidTransaction(sender, receiver, bankTransfer);
        assertThrows(FailedTransactionException.class, () -> transactionService.processPayment(transaction));
    }

    @Test
    public void processPayment_whenReceiverIsDeactivated_thenThrows_FailedTransactionException() {
        BankAccount sender = initValidBankAccount(initValidSenderCustomer(), BigDecimal.valueOf(500));
        BankAccount receiver = initValidBankAccount(initValidReceiverCustomer(),  BigDecimal.ZERO);

        BankTransfer bankTransfer = initValidBankTransfer(sender);

        sender.activateBankAccount();
        receiver.deactivateBankAccount();
        Transaction transaction = initValidTransaction(sender, receiver, bankTransfer);
        assertThrows(FailedTransactionException.class, () -> transactionService.processPayment(transaction));
    }

    @Test
    public void processPayment_whenSender_hasInsufficientFunds_thenThrows_FailedTransactionException() {
        BankAccount sender = initValidBankAccount(initValidSenderCustomer(), BigDecimal.valueOf(500));
        BankAccount receiver = initValidBankAccount(initValidReceiverCustomer(), BigDecimal.ZERO);

        BankTransfer bankTransfer = initValidBankTransfer(sender);

        Transaction transaction = initValidTransaction(sender, receiver, bankTransfer);
        transaction.setAmount(BigDecimal.valueOf(1000));
        assertThrows(FailedTransactionException.class, () -> transactionService.processPayment(transaction));
    }

    @Test
    public void processPayment_whenTransaction_isNull() {
        assertThrows(DAOException.class, () -> transactionService.processPayment(null));
    }

    @Test
    public void processPayment_whenTransactionDAO_fails() {
        BankAccount sender = initValidBankAccount(initValidSenderCustomer(), BigDecimal.valueOf(500));
        BankAccount receiver = initValidBankAccount(initValidReceiverCustomer(), BigDecimal.ZERO);
        BankTransfer bankTransfer = initValidBankTransfer(sender);
        Transaction transaction = initValidTransaction(sender, receiver, bankTransfer);

        Mockito.when(transactionDAO.save(any())).thenThrow(RuntimeException.class);

        assertThrows(DAOException.class, () -> transactionService.processPayment(transaction));
    }

    @Test
    public void findByBankAccount_whenBankAccount_isValid() {
        BankAccount bankAccount = initValidBankAccount(initValidSenderCustomer(), BigDecimal.valueOf(500));
        
        List<Transaction> expectedTransactionList = new ArrayList<>();
        expectedTransactionList.add(new Transaction());
        expectedTransactionList.add(new Transaction());
        expectedTransactionList.add(new Transaction());
        
        Mockito.when(transactionDAO.findBySenderBankAccount(bankAccount)).thenReturn(expectedTransactionList);
        
        List<Transaction> actualTransactionList = transactionService.findByBankAccount(bankAccount);

        assertEquals(expectedTransactionList, actualTransactionList);
    }

    @Test
    public void findByBankAccount_whenTransactionDAO_fails() {
        BankAccount bankAccount = initValidBankAccount(initValidSenderCustomer(), BigDecimal.valueOf(500));

        Mockito.when(transactionDAO.findBySenderBankAccount(bankAccount)).thenThrow(RuntimeException.class);

        assertThrows(DAOException.class, () -> transactionService.findByBankAccount(bankAccount));
    }

    @Test
    public void findByBankAccount_whenBankAccount_isNull() {
        assertThrows(DAOException.class, () -> transactionService.findByBankAccount(null));
    }

    @Test
    public void findById_whenId_isValid() {
        Long id = 1L;

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setId(id);

        Mockito.when(transactionDAO.findById(id)).thenReturn(expectedTransaction);

        Transaction actualTransaction = transactionService.findById(id);

        assertEquals(actualTransaction.getId(), expectedTransaction.getId());
    }

    @Test
    public void findById_whenId_isNull() {
        assertThrows(DAOException.class, () -> transactionService.findById(null));
    }

    @Test
    public void findById_whenNoResult() {
        Mockito.when(transactionDAO.findById(any())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> transactionService.findById(1L));
    }

    @Test
    public void findById_whenTransactionDAO_fails() {
        Mockito.when(transactionDAO.findById(any())).thenThrow(RuntimeException.class);

        assertThrows(DAOException.class, () -> transactionService.findById(1L));
    }

    private static BankTransfer initValidBankTransfer(BankAccount sender) {
        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setBankAccount(sender);
        return bankTransfer;
    }

    private static BankAccount initValidBankAccount(Customer customer, BigDecimal accountBalance) {
        BankAccount receiver = new BankAccount();
        receiver.activateBankAccount();
        receiver.setAccountBalance(accountBalance);
        receiver.setAccountHolder(customer);
        return receiver;
    }

    private static Customer initValidReceiverCustomer() {
        Customer receiverCustomer = new Customer();
        receiverCustomer.setAddress("Receiver Address");
        receiverCustomer.setFirstName("Receiver First Name");
        return receiverCustomer;
    }

    private static Customer initValidSenderCustomer() {
        Customer senderCustomer = new Customer();
        senderCustomer.setAddress("Sender Address");
        senderCustomer.setFirstName("Sender First Name");
        return senderCustomer;
    }

    private static Transaction initValidTransaction(BankAccount sender, BankAccount receiver, BankTransfer bankTransfer) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setPaymentInstrument(bankTransfer);
        return transaction;
    }
}
