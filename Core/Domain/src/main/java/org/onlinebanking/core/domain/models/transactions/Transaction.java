package org.onlinebanking.core.domain.models.transactions;

import javax.persistence.*;
import org.onlinebanking.core.domain.models.BankAccount;
import org.onlinebanking.core.domain.models.Identifiable;
import org.onlinebanking.core.domain.models.paymentinstruments.PaymentInstrument;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;
    @Column(name = "transaction_date")
    private Timestamp transactionDate;
    @Column(name = "transaction_name")
    private String transactionName;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private BankAccount sender;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private BankAccount receiver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_instrument_id")
    private PaymentInstrument paymentInstrument;
    @Column(name = "amount")
    private BigDecimal amount;

    public Transaction() {

    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setSender(BankAccount sender) {
        this.sender = sender;
    }

    public void setReceiver(BankAccount receiver) {
        this.receiver = receiver;
    }

    public PaymentInstrument getPaymentInstrument() {
        return paymentInstrument;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public BankAccount getReceiver() {
        return receiver;
    }

    public BankAccount getSender() {
        return sender;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(transactionName, that.transactionName) && transactionType == that.transactionType && transactionStatus == that.transactionStatus && Objects.equals(sender, that.sender) && Objects.equals(receiver, that.receiver) && Objects.equals(paymentInstrument, that.paymentInstrument) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionDate, transactionName, transactionType, transactionStatus, sender, receiver, paymentInstrument, amount);
    }
}
