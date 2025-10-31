package models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Payments")
@NamedQueries({
        @NamedQuery(
                name = "Payment.findAll",
                query = "SELECT p FROM Payment p"
        ),
        @NamedQuery(
                name = "Payment.findBySubscription",
                query = "SELECT p FROM Payment p WHERE p.subscriptionId = :subscriptionId"
        ),
        @NamedQuery(
                name = "Payment.sumBySubscription",
                query = "SELECT SUM(p.amount) FROM Payment p WHERE p.subscriptionId = :subscriptionId"
        )
})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int id;

    @Column(name = "subscription_id", nullable = false)
    private int subscriptionId;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "payment_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date paymentDate;

    public Payment() {}

    public Payment(int id, int subscriptionId, double amount, Date paymentDate) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public Payment(int subscriptionId, double amount, Date paymentDate) {
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(int subscriptionId) { this.subscriptionId = subscriptionId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", subscriptionId=" + subscriptionId +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                '}';
    }
}