package org.onlinebanking.core.domain.models.paymentinstruments;

import org.onlinebanking.core.domain.dto.BankTransferDTO;

import javax.persistence.*;

@Entity
@Table(name = "bank_transfer")
@DiscriminatorValue("bank_transfer")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public class BankTransfer extends PaymentInstrument {

    public BankTransfer(BankTransferDTO bankTransferDTO) {
        this.bankAccount = bankTransferDTO.getBankAccount();
    }

    public BankTransfer() {

    }

    @Override
    public String getDescription() {
        return "Bank Transfer";
    }

    @Override
    public PaymentInstrumentType getPaymentInstrumentType() {
        return PaymentInstrumentType.BANK_TRANSFER;
    }
}
