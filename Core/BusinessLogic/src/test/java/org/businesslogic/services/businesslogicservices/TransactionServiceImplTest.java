package org.businesslogic.services.businesslogicservices;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.businesslogicservices.TransactionServiceImpl;
import org.onlinebanking.core.dataaccess.dao.interfaces.TransactionDAO;
import org.onlinebanking.core.domain.dto.TransactionDTO;
import org.onlinebanking.core.domain.models.BankAccount;

import static junit.framework.Assert.*;
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
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionDAO, bankAccountService, paymentInstrumentService);
    }

    // logic is the same for receiver i.e. if receiver has been deactivated instead of sender, the result would be the same
    @Test
    public void processPayment_whenSenderIsDeactivated_thenFalse() {
        BankAccount sender = new BankAccount();
        BankAccount receiver = new BankAccount();
        sender.deactivateBankAccount();
        receiver.activateBankAccount();
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(sender);
        transactionDTO.setReceiver(receiver);

        assertFalse(transactionService.processPayment(transactionDTO));
    }

}
