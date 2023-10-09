package org.onlinebanking.core.domain.models.paymentinstruments;

import org.onlinebanking.core.domain.models.paymentinstruments.cards.DebitCard;

import javax.persistence.*;
import java.util.Objects;

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
