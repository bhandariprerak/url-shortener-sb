package com.url.shortener.repository;

import com.url.shortener.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // These methods are not strictly “inbuilt” methods of JpaRepository, but Spring Data JPA generates them for you automatically based on their naming convention.
    // Spring Data JPA will automatically translate them into SQL queries
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

