package com.url.shortener.repository;

import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing UrlMapping entities.
 * Provides methods to perform CRUD operations and custom queries related to URL mappings.
 */
@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    /**
     * Finds a UrlMapping entity by its short URL.
     *
     * @param shortUrl the short URL string to search for
     * @return the UrlMapping entity matching the given short URL, or null if none found
     */
    UrlMapping findByShortUrl(String shortUrl);

    /**
     * Retrieves all UrlMapping entities associated with a specific user.
     *
     * @param user the User entity whose URL mappings are to be retrieved
     * @return a list of UrlMapping entities belonging to the specified user
     */
    List<UrlMapping> findByUser(User user);
}