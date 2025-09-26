package com.url.shortener.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing the mapping between an original URL and its shortened version.
 * Stores information such as the original URL, the generated short URL, click count, creation date,
 * associated user, and related click events.
 */
@Entity // @Entity makes it a table (url_mapping by default, unless overridden).
@Data
public class UrlMapping {
    /** Unique identifier for each URL mapping. */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /** The original long URL to be shortened. */
    private String originalUrl;

    /** The generated short URL. Can be null initially until assigned. */
    @Column(nullable = true)
    private String shortUrl;

    /** Number of times the short URL was clicked. */
    private int clickCount = 0;

    /** Timestamp when this mapping was created. */
    private LocalDateTime createdDate;

    /**
     * The user who owns this URL mapping.
     * Many URLs can belong to one user.
     */
    @ManyToOne // This says: “Many URLs belong to one user.”
    @JoinColumn(name = "user_id") // Creates a user_id foreign key column in the url_mapping table.
    private User user;

    /**
     * List of click events associated with this URL mapping.
     * One URL mapping can have many click events.
     */
    @OneToMany(mappedBy = "urlMapping") // This says: “One URL mapping can have many click events.” mappedBy = "urlMapping" means the ClickEvent entity has a field named urlMapping that defines the relationship.
    private List<ClickEvent> clickEvents;
}
