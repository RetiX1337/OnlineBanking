package org.onlinebanking.core.domain.models.paymentinstruments;

import org.onlinebanking.core.domain.servicedto.paymentinstruments.BankTransferServiceDTO;
import org.onlinebanking.core.domain.models.BankAccount;

import javax.persistence.*;

@Entity
@Table(name = "bank_transfer")
@DiscriminatorValue("bank_transfer")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public class BankTransfer extends PaymentInstrument {

    public BankTransfer(BankTransferServiceDTO bankTransferServiceDTO) {
        setBankAccount(bankTransferServiceDTO.getBankAccount());
    }

    public BankTransfer() {

    }


    @Override
    public String getDescription() {
        return "Bank Transfer";
    }

}
