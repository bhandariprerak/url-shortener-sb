package com.url.shortener.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity // @Entity makes it a table (url_mapping by default, unless overridden).
@Data
public class UrlMapping {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount = 0;
    private LocalDate createdDate;

    @ManyToOne // This says: “Many URLs belong to one user.”
    @JoinColumn(name = "user_id") // Creates a user_id foreign key column in the url_mapping table.
    private User user;

    @OneToMany(mappedBy = "urlMapping") // This says: “One URL mapping can have many click events.” mappedBy = "urlMapping" means the ClickEvent entity has a field named urlMapping that defines the relationship.
    private List<ClickEvent> clickEvents;
}
