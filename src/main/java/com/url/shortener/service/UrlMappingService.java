package com.url.shortener.service;

import com.url.shortener.dtos.ClickEventDTO;
import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.models.ClickEvent;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import com.url.shortener.repository.ClickEventRepository;
import com.url.shortener.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing URL mappings, generating short URLs, tracking click events,
 * and providing analytics for users in the URL shortener application.
 */
@Service
@AllArgsConstructor
public class UrlMappingService {

    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;

    /**
     * Creates a new shortened URL mapping for the given original URL and user.
     * Saves the mapping, generates a short URL based on the auto-generated ID (using base62 encoding), updates the mapping,
     * and returns its DTO representation.
     *
     * @param originalUrl The original long URL to shorten.
     * @param user The user creating the shortened URL.
     * @return UrlMappingDTO representing the saved URL mapping.
     */
    public UrlMappingDTO createShortUrl(String originalUrl, User user) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        // Save first to get the generated ID
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        String shortUrl = encodeIdToShortUrl(savedUrlMapping.getId());
        savedUrlMapping.setShortUrl(shortUrl);
        savedUrlMapping = urlMappingRepository.save(savedUrlMapping);
        return convertToDto(savedUrlMapping);
    }

    /**
     * Converts a UrlMapping entity to its DTO representation.
     *
     * @param urlMapping The UrlMapping entity.
     * @return UrlMappingDTO containing mapped data.
     */
    private UrlMappingDTO convertToDto(UrlMapping urlMapping){
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
        return urlMappingDTO;
    }

    /**
     * Encodes the given numeric ID to a base62 short URL string.
     * The resulting string is URL-safe and compact.
     *
     * @param id The numeric ID to encode.
     * @return The base62-encoded string.
     */
    private String encodeIdToShortUrl(long id) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl = new StringBuilder();
        if (id == 0) {
            return String.valueOf(characters.charAt(0));
        }
        while (id > 0) {
            int rem = (int)(id % 62);
            shortUrl.insert(0, characters.charAt(rem));
            id = id / 62;
        }
        return shortUrl.toString();
    }

    /**
     * Retrieves all URL mappings created by a specific user, converted to DTOs.
     *
     * @param user The user whose URLs are to be retrieved.
     * @return List of UrlMappingDTOs for the user.
     */
    public List<UrlMappingDTO> getUrlsByUser(User user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Retrieves click events for a given short URL between the specified start and end date-times,
     * grouped by date, and returns them as a list of ClickEventDTOs.
     *
     * @param shortUrl The short URL to fetch click events for.
     * @param start The start datetime of the range.
     * @param end The end datetime of the range.
     * @return List of ClickEventDTOs grouped by date, or null if the URL is not found.
     */
    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
                    .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> {
                        ClickEventDTO clickEventDTO = new ClickEventDTO();
                        clickEventDTO.setClickDate(entry.getKey());
                        clickEventDTO.setCount(entry.getValue());
                        return clickEventDTO;
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Retrieves the total number of clicks for all URLs belonging to a user within a date range,
     * grouped by date.
     *
     * @param user The user whose click data is to be retrieved.
     * @param start The start date (inclusive).
     * @param end The end date (inclusive).
     * @return Map of LocalDate to total click count for each day in the range.
     */
    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        return clickEvents.stream()
                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));

    }

    /**
     * Retrieves the original URL mapping for a given short URL, increments its click count,
     * and records a click event.
     *
     * @param shortUrl The short URL to resolve.
     * @return The UrlMapping entity, or null if not found.
     */
    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);

            // Record Click Event
            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);
        }

        return urlMapping;
    }
}