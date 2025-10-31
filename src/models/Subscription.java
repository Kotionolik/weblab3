package models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Subscriptions")
@NamedQueries({
        @NamedQuery(
                name = "Subscription.findAll",
                query = "SELECT s FROM Subscription s"
        ),
        @NamedQuery(
                name = "Subscription.findByUser",
                query = "SELECT s FROM Subscription s WHERE s.userId = :userId"
        ),
        @NamedQuery(
                name = "Subscription.findUnpaid",
                query = "SELECT s FROM Subscription s " +
                        "WHERE (SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.subscriptionId = s.id) < " +
                        "(SELECT pub.pricePerMonth FROM Publication pub WHERE pub.id = s.publicationId) * s.months"
        )
})
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "publication_id", nullable = false)
    private int publicationId;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "months", nullable = false)
    private int months;

    public Subscription() {}

    public Subscription(int id, int userId, int publicationId, Date startDate, int months) {
        this.id = id;
        this.userId = userId;
        this.publicationId = publicationId;
        this.startDate = startDate;
        this.months = months;
    }

    public Subscription(int userId, int publicationId, Date startDate, int months) {
        this.userId = userId;
        this.publicationId = publicationId;
        this.startDate = startDate;
        this.months = months;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getPublicationId() { return publicationId; }
    public void setPublicationId(int publicationId) { this.publicationId = publicationId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public int getMonths() { return months; }
    public void setMonths(int months) { this.months = months; }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", userId=" + userId +
                ", publicationId=" + publicationId +
                ", startDate=" + startDate +
                ", months=" + months +
                '}';
    }
}