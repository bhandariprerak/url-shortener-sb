package com.url.shortener.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

// DTO for handling user login requests.
// Keeps authentication input clean and separate from DB models.
// Lombok @Data generates getters/setters automatically.