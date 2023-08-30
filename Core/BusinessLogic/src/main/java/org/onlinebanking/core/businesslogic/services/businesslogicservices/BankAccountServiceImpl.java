package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import org.iban4j.Iban;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.dataaccess.dao.interfaces.BankAccountDAO;
import org.onlinebanking.core.domain.dto.BankTransferDTO;
import org.onlinebanking.core.domain.dto.TransactionDTO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountDAO bankAccountDAO;
    private final PaymentInstrumentService paymentInstrumentService;

    @Autowired
    public BankAccountServiceImpl(BankAccountDAO bankAccountDAO, PaymentInstrumentService paymentInstrumentService) {
        this.bankAccountDAO = bankAccountDAO;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    @Transactional
    @Override
    public void openBankAccount(Customer customer) {
        boolean isUnique = false;
        String iban = getIban(isUnique);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountBalance(BigDecimal.ZERO);
        bankAccount.setAccountNumber(iban);
        bankAccount.setAccountHolder(customer);
        bankAccount.activateBankAccount();
        bankAccountDAO.save(bankAccount);

        paymentInstrumentService.openPaymentInstrument(initBankTransfer(bankAccount));
    }

    @Transactional
    @Override
    public boolean activateBankAccount(BankAccount bankAccount) {
        if (!isPresent(bankAccount)) {
            return false;
        }
        if (!bankAccount.isActive()) {
            bankAccount.activateBankAccount();
            bankAccountDAO.update(bankAccount);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deactivateBankAccount(BankAccount bankAccount) {
        if (!isPresent(bankAccount)) {
            return false;
        }
        if (bankAccount.isActive()) {
            bankAccount.deactivateBankAccount();
            bankAccountDAO.update(bankAccount);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public BankAccount updateBankAccount(BankAccount bankAccount) {
        return bankAccountDAO.update(bankAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BankAccount> findByCustomer(Customer customer) {
        return bankAccountDAO.findByCustomer(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return bankAccountDAO.findByAccountNumber(accountNumber);
    }

    private String getIban(boolean isUnique) {
        String iban;
        do {
            iban = Iban.random().getAccountNumber();
            if (bankAccountDAO.findByAccountNumber(iban) == null) {
                isUnique = true;
            }
        } while (!isUnique);
        return iban;
    }

    private boolean isPresent(BankAccount bankAccount) {
        return bankAccountDAO.findByAccountNumber(bankAccount.getAccountNumber()) != null;
    }

    private BankTransferDTO initBankTransfer(BankAccount bankAccount) {
        BankTransferDTO bankTransferDTO = new BankTransferDTO();
        bankTransferDTO.setBankAccount(bankAccount);
        return bankTransferDTO;
    }

}
