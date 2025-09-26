package com.url.shortener.controller;

import com.url.shortener.dtos.ClickEventDTO;
import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.models.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing URL shortening and analytics.
 * Provides endpoints for creating short URLs, retrieving user-specific URLs,
 * fetching click analytics for short URLs, and obtaining total click counts by date.
 * All endpoints require authenticated users with 'USER' role.
 */
@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
    private UrlMappingService urlMappingService;
    private UserService userService;

    /**
     * Creates a shortened URL for the given original URL.
     * Requires authenticated user with role 'USER'.
     * @param request JSON containing "originalUrl" key
     * @param principal Security principal containing authenticated user info
     * @return UrlMappingDTO with details of the shortened URL
     */
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request,
                                                        Principal principal){
        String originalUrl = request.get("originalUrl");
        // Retrieve authenticated user by username
        User user = userService.findByUsername(principal.getName());
        // Delegate to service to create short URL mapping
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }


    /**
     * Retrieves all shortened URLs created by the authenticated user.
     * Requires authenticated user with role 'USER'.
     * @param principal Security principal containing authenticated user info
     * @return List of UrlMappingDTO objects representing user's URLs
     */
    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal){
        // Retrieve user by username
        User user = userService.findByUsername(principal.getName());
        // Fetch URLs associated with the user
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }


    /**
     * Retrieves click analytics for a specific short URL within a date-time range.
     * Requires authenticated user with role 'USER'.
     * @param shortUrl The short URL identifier
     * @param startDate Start date-time in ISO_LOCAL_DATE_TIME format
     * @param endDate End date-time in ISO_LOCAL_DATE_TIME format
     * @return List of ClickEventDTO representing click events in the time range
     */
    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl,
                                                               @RequestParam("startDate") String startDate,
                                                               @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        // Parse date-time strings to LocalDateTime objects
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        // Retrieve click events for the short URL within the date-time range
        List<ClickEventDTO> clickEventDTOS = urlMappingService.getClickEventsByDate(shortUrl, start, end);
        return ResponseEntity.ok(clickEventDTOS);
    }


    /**
     * Retrieves total click counts grouped by date for the authenticated user
     * within the specified date range.
     * Requires authenticated user with role 'USER'.
     * @param principal Security principal containing authenticated user info
     * @param startDate Start date in ISO_LOCAL_DATE format
     * @param endDate End date in ISO_LOCAL_DATE format
     * @return Map of LocalDate to Long representing total clicks per day
     */
    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal,
                                                                     @RequestParam("startDate") String startDate,
                                                                     @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        // Retrieve user by username
        User user = userService.findByUsername(principal.getName());
        // Parse date strings to LocalDate objects
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        // Fetch total clicks grouped by date for the user
        Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user, start, end);
        return ResponseEntity.ok(totalClicks);
    }
}