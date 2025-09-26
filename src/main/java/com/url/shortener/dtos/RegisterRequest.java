package com.url.shortener.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private Set<String> role;
    private String password;
}

// DTO for handling user registration requests.
// Keeps input fields separate from the database User entity.
// Lombok @Data generates all boilerplate getters/setters.