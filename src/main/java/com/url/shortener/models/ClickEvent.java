package com.url.shortener.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ClickEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime clickDate; // Currently, I'm just storing the date and time when the URL was clicked

    @ManyToOne
    @JoinColumn(name = "url_mapping_id")
    private UrlMapping urlMapping;
}

// Later, I might add more details for analytics:
//	•	ipAddress
//	•	deviceType
//	•	referrer (what site they came from)
//	•	country (geo-IP lookup)