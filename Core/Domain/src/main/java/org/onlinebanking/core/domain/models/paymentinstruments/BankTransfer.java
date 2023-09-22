package org.onlinebanking.core.domain.models.paymentinstruments;

import org.onlinebanking.core.domain.dto.requests.paymentinstruments.BankTransferRequest;
import org.onlinebanking.core.domain.models.BankAccount;

import javax.persistence.*;

@Entity
@Table(name = "bank_transfer")
@DiscriminatorValue("bank_transfer")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public class BankTransfer extends PaymentInstrument {

    public BankTransfer(BankTransferRequest bankTransferRequest, BankAccount bankAccount) {
        setBankAccount(bankAccount);
    }

    public BankTransfer() {

    }


    @Override
    public String getDescription() {
        return "Bank Transfer";
    }

}
