package com.url.shortener.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity // @Entity → Tells JPA this class = a DB table.
@Data // Lombok feature. Generates getters, setters, toString(), equals(), hashCode().
@Table(name = "users") // @Table(name = "users") (if present) tells it what table name to use — if you don’t specify, it defaults to the class name (User → user).
public class User {
    @Id // @Id marks the primary key of the table.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue(strategy = GenerationType.IDENTITY) tells the DB to auto-increment IDs.
    private Long id; // Long → Chosen because IDs can get very large as users grow.
    @Column(nullable = false, unique = true)
    private String email;
    private String username;
    private String password;
    private String role = "ROLE_USER"; // The role field defaults to "ROLE_USER". Spring Security expects roles to be prefixed with "ROLE_", so this is already aligned with best practices

}

// @Column(name = "email", nullable = false, unique = true) → this controls the column properties.
//	If you don’t put @Column, Spring just maps the variable name to a column name.