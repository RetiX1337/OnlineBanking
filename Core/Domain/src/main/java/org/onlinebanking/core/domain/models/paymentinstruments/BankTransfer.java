package org.onlinebanking.core.domain.models.paymentinstruments;

import javax.persistence.*;

@Entity
@Table(name = "bank_transfer")
@DiscriminatorValue("bank_transfer")
@PrimaryKeyJoinColumn(name = "payment_instrument_id")
public class BankTransfer extends PaymentInstrument {

    public BankTransfer() {

    }


    @Override
    public String getDescription() {
        return "Bank Transfer";
    }

}
