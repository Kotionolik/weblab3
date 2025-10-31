package models;

import jakarta.persistence.*;

@Entity
@Table(name = "Publications")
@NamedQueries({
        @NamedQuery(
                name = "Publication.findAll",
                query = "SELECT p FROM Publication p"
        ),
        @NamedQuery(
                name = "Publication.findByTitle",
                query = "SELECT p FROM Publication p WHERE p.title = :title"
        ),
        @NamedQuery(
                name = "Publication.findByPublisher",
                query = "SELECT p FROM Publication p WHERE p.publisher = :publisher"
        )
})
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publication_id")
    private int id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "publisher", nullable = false, length = 100)
    private String publisher;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_per_month", nullable = false)
    private double pricePerMonth;

    public Publication() {}

    public Publication(int id, String title, String publisher, String description, double pricePerMonth) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.description = description;
        this.pricePerMonth = pricePerMonth;
    }

    public Publication(String title, String publisher, String description, double pricePerMonth) {
        this.title = title;
        this.publisher = publisher;
        this.description = description;
        this.pricePerMonth = pricePerMonth;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return pricePerMonth; }
    public double getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(double pricePerMonth) { this.pricePerMonth = pricePerMonth; }

    @Override
    public String toString() {
        return "Publication{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pricePerMonth=" + pricePerMonth +
                '}';
    }
}