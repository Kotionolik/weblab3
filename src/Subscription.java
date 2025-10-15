import java.sql.Date;

public class Subscription {
    private int id;
    private int user_id;
    private int publication_id;
    private Date startDate;
    private int months;

    public Subscription(int id, int user_id, int publication_id, Date startDate, int months) {
        this.id = id;
        this.user_id = user_id;
        this.publication_id = publication_id;
        this.startDate = startDate;
        this.months = months;
    }

    public Subscription(int user_id, int publication_id, Date startDate, int months) {
        this(0, user_id, publication_id, startDate, months);
    }

    public int getId() { return id; }
    public int getUserId() { return user_id; }
    public int getPublicationId() { return publication_id; }
    public Date getStartDate() { return startDate; }
    public int getMonths() { return months; }

}
