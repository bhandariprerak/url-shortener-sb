package com.url.shortener.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
/**
 * Data Transfer Object (DTO) representing a mapping between an original URL and its shortened version.
 * <p>
 * Fields:
 * <ul>
 *   <li>id - Unique identifier for the URL mapping.</li>
 *   <li>originalUrl - The original (long) URL to be shortened.</li>
 *   <li>shortUrl - The generated shortened URL.</li>
 *   <li>clickCount - Number of times the short URL has been accessed.</li>
 *   <li>createdDate - Timestamp when the mapping was created.</li>
 *   <li>username - Username of the user who created the mapping.</li>
 * </ul>
 */
public class UrlMappingDTO {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdDate;
    private String username;
}