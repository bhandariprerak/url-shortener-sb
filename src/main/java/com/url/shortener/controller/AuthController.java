package com.url.shortener.controller;

import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.dtos.RegisterRequest;
import com.url.shortener.models.User;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST Controller for authentication (login + register endpoints).
// Base path = /api/auth
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService; // Handles business logic for authentication/registration

    // POST /api/auth/public/login
    // Accepts LoginRequest DTO, delegates authentication to UserService
    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    // POST /api/auth/public/register
    // Accepts RegisterRequest DTO, maps it to User entity, sets default role, and saves via UserService
    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword()); // encoding password in Userservice.java using the BCryptPasswordEncoder already configured in WebSecurityConfig.
        user.setEmail(registerRequest.getEmail());
        user.setRole("ROLE_USER"); // Default role for new users
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
}