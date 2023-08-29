package org.onlinebankingweb.controllers;

import org.onlinebanking.core.businesslogic.factories.PaymentInstrumentFactory;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.businesslogic.services.businesslogicservices.PaymentInstrumentServiceImpl;
import org.onlinebanking.core.dataaccess.dao.interfaces.BankAccountDAO;
import org.onlinebanking.core.dataaccess.dao.interfaces.CustomerDAO;
import org.onlinebanking.core.domain.dto.CreditCardDTO;
import org.onlinebanking.core.domain.dto.DebitCardDTO;
import org.onlinebanking.core.domain.dto.PaymentInstrumentDTO;
import org.onlinebanking.core.domain.models.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final CustomerDAO customerDAO;
    private final BankAccountDAO bankAccountDAO;
    private final PaymentInstrumentService paymentInstrumentService;

    @Autowired
    public UserController(UserService userService, CustomerDAO customerDAO, BankAccountDAO bankAccountDAO, PaymentInstrumentService paymentInstrumentService) {
        this.userService = userService;
        this.customerDAO = customerDAO;
        this.bankAccountDAO = bankAccountDAO;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    @GetMapping("/starter")
    public String testStarterPage() {
        return "test";
    }

    @GetMapping("/test")
    public String testMethod() {
        System.out.println("method entered");

        DebitCardDTO debitCardDTO = new DebitCardDTO();
        debitCardDTO.setBankAccount(bankAccountDAO.findById(1L));
        debitCardDTO.setDailyWithdrawalLimit(BigDecimal.valueOf(10000));
        debitCardDTO.setDailyTransactionLimit(10);
        debitCardDTO.setCVVHash("grepropgorp");
        debitCardDTO.setPINHash("543253523525");

        PaymentInstrumentDTO paymentInstrumentDTO = debitCardDTO;

        paymentInstrumentService.openPaymentInstrument(paymentInstrumentDTO);
        return "result";
    }


}
