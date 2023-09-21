package org.businesslogic.services.businesslogicservices;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.impl.TransactionServiceImpl;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.dto.requests.TransactionRequest;
import org.onlinebanking.core.domain.exceptions.FailedTransactionException;
import org.onlinebanking.core.domain.models.BankAccount;

import static org.junit.Assert.assertThrows;

public class TransactionServiceImplTest {
    @Mock
    private TransactionDAO transactionDAO;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private PaymentInstrumentService paymentInstrumentService;
    private TransactionServiceImpl transactionService;

    @Before
    public void setUp() {
        transactionService = new TransactionServiceImpl(transactionDAO, bankAccountService, paymentInstrumentService);
    }

    // logic is the same for receiver i.e. if receiver has been deactivated instead of sender, the result would be the same
    @Test(expected = FailedTransactionException.class)
    public void processPayment_whenSenderIsDeactivated_thenThrows_FailedTransactionException() {
        BankAccount sender = new BankAccount();
        BankAccount receiver = new BankAccount();
        sender.deactivateBankAccount();
        receiver.activateBankAccount();
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setSender(sender);
        transactionRequest.setReceiver(receiver);
        transactionService.processPayment(transactionRequest);
    }

}
