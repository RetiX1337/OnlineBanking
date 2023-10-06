package org.onlinebankingweb.controllers.rest;

import com.google.common.base.Preconditions;
import org.checkerframework.checker.units.qual.A;
import org.onlinebanking.core.businesslogic.services.BankAccountService;
import org.onlinebanking.core.businesslogic.services.PaymentInstrumentService;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrumentType;
import org.onlinebankingweb.dto.requests.paymentinstruments.BankTransferRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.PaymentInstrumentRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.CreditCardRequest;
import org.onlinebankingweb.dto.requests.paymentinstruments.cards.DebitCardRequest;
import org.onlinebankingweb.dto.responses.paymentinstruments.PaymentInstrumentResponse;
import org.onlinebankingweb.factories.PaymentInstrumentFactory;
import org.onlinebankingweb.factories.PaymentInstrumentResponseFactory;
import org.onlinebankingweb.mappers.PaymentInstrumentMapper;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-instrument")
public class PaymentInstrumentRestController {
    private final BankAccountService bankAccountService;
    private final PaymentInstrumentService paymentInstrumentService;
    private final PaymentInstrumentFactory paymentInstrumentFactory;
    private final PaymentInstrumentMapper paymentInstrumentMapper;

    @Autowired
    public PaymentInstrumentRestController(BankAccountService bankAccountService,
                                           PaymentInstrumentService paymentInstrumentService,
                                           PaymentInstrumentFactory paymentInstrumentFactory,
                                           PaymentInstrumentMapper paymentInstrumentMapper) {
        this.bankAccountService = bankAccountService;
        this.paymentInstrumentService = paymentInstrumentService;
        this.paymentInstrumentFactory = paymentInstrumentFactory;
        this.paymentInstrumentMapper = paymentInstrumentMapper;
    }

    @GetMapping("/accounts/{accNumber}")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public List<PaymentInstrumentResponse> getBankAccountPaymentInstruments(@PathVariable("accNumber") String accNumber,
                                                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(accNumber);
        BankAccount bankAccount = bankAccountService.findByAccountNumber(accNumber);

        if (!bankAccount.getAccountHolder().getUser().getId().equals(userPrincipal.getUserId())) {
            throw new AccessDeniedException("The Bank Account doesn't belong to the logged in User");
        }

        return paymentInstrumentMapper.responseListByBankAccount(bankAccount);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public PaymentInstrumentResponse getPaymentInstrument(@PathVariable("id") Long id,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(id);
        PaymentInstrument paymentInstrument = paymentInstrumentService.findById(id);

        if (!paymentInstrument.getBankAccount().getAccountHolder().getUser().getId().equals(userPrincipal.getUserId())) {
            throw new AccessDeniedException("The Payment Instrument doesn't belong to the logged in User");
        }

        return PaymentInstrumentResponseFactory.createPaymentInstrument(paymentInstrument);
    }

    @PostMapping("/")
    @PreAuthorize("isAuthenticated() && hasRole('USER_ROLE')")
    public PaymentInstrumentResponse openPaymentInstrument(@RequestBody PaymentInstrumentRequest paymentInstrumentRequest,
                                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(paymentInstrumentRequest);

        BankAccount bankAccount = bankAccountService.findByAccountNumber(paymentInstrumentRequest.getBankAccountNumber());
        if (!bankAccount.getAccountHolder().getUser().getId().equals(userPrincipal.getUserId())) {
            throw new AccessDeniedException("The Bank Account is not related to the logged in User");
        }
        PaymentInstrument paymentInstrument
                = paymentInstrumentFactory.createPaymentInstrument(paymentInstrumentRequest, bankAccount);

        PaymentInstrument savedPaymentInstrument = paymentInstrumentService.openPaymentInstrument(paymentInstrument);

        return PaymentInstrumentResponseFactory.createPaymentInstrument(savedPaymentInstrument);
    }
}
