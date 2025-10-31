package models;

public class Publication {
    private int id;
    private String title;
    private String publisher;
    private String description;
    private double price_per_month;

    public Publication(int id, String title, String publisher, String description, double price_per_month) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.description = description;
        this.price_per_month = price_per_month;
    }

    public Publication(String title, String publisher, String description, double price_per_month) {
        this(0, title, publisher, description, price_per_month);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPublisher() { return publisher; }
    public String getDescription() { return description; }
    public double getPrice() { return price_per_month; }

}
