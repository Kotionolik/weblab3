package models;

import java.sql.Date;

public class Payment {
    private int id;
    private int subscription_id;
    private double amount;
    private Date paymentDate;

    public Payment(int id, int subscription_id, double amount, Date paymentDate) {
        this.id = id;
        this.subscription_id = subscription_id;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public Payment(int subscription_id, double amount, Date paymentDate) {
        this(0, subscription_id, amount, paymentDate);
    }

    public int getId() { return id; }
    public int getSubscriptionId() { return subscription_id; }
    public double getAmount() { return amount; }
    public Date getPaymentDate() { return paymentDate; }

}
