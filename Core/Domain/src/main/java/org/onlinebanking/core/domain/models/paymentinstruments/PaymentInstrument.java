package org.onlinebanking.core.domain.models.paymentinstruments;

import javax.persistence.*;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Identifiable;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@Table(name = "payment_instruments")
public abstract class PaymentInstrument implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_instrument_id")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    public PaymentInstrument() {

    }

    public boolean withdraw(BigDecimal amount) {
        return bankAccount.withdraw(amount);
    }

    public void deposit(BigDecimal amount) {
        bankAccount.deposit(amount);
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public abstract String getDescription();

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }
}
