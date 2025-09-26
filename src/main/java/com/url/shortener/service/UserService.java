package com.url.shortener.service;

import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.exceptions.UserAlreadyExistsException;
import com.url.shortener.models.User;
import com.url.shortener.repository.UserRepository;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;
import com.url.shortener.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
/**
 * Service class for managing user-related operations such as registration,
 * authentication, and retrieval by username.
 */
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    /**
     * Registers a new user after validating that the email and username are unique.
     * Password is encoded before saving.
     *
     * @param user The user to register
     * @return The saved user entity
     * @throws UserAlreadyExistsException if email or username is already taken
     */
    public User registerUser(User user){
        // Check if email is already in use
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        // Check if username is already taken
        else if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        // Encode the user's password for security
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        // Save and return the new user
        return userRepository.save(user);
    }

    /**
     * Authenticates a user using username and password, generates a JWT token upon success.
     *
     * @param loginRequest Contains username and password
     * @return JWT authentication response with token
     */
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest){
        // Perform authentication using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        // Set authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Retrieve user details from authentication principal
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Generate JWT token for the authenticated user
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Finds a user by username.
     *
     * @param name The username to search for
     * @return The user entity if found
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    public User findByUsername(String name) {
        // Retrieve user or throw exception if not found
        return userRepository.findByUsername(name).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + name)
        );
    }
}