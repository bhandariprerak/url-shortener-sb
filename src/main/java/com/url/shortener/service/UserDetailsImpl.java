/*
About this file:
UserDetailsImpl is my custom implementation of Spring Security’s UserDetails interface.
Spring Security needs this to understand my User entity (from my database) in a way it can work with.

Think of it as an adapter:
Your User class (database entity) → wrapped into UserDetailsImpl → Spring Security understands it.

 Why do we need this?
	•	Spring Security doesn’t know about your User class.
	•	It only works with its own UserDetails model.
	•	So this UserDetailsImpl is like a translator between your database users and Spring Security.
 */


package com.url.shortener.service;

import com.url.shortener.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails { // UserDetails is a Spring Security interface.
    // serialVersionUID is a unique version number for a Serializable class.
    // It ensures that deserialization works correctly even if the class definition changes slightly.
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private String password;

    private Collection<? extends GrantedAuthority> authorities; // 	authorities = roles/permissions (ROLE_USER, ROLE_ADMIN, etc.) → tells Spring what this user can do.

    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) { // Converts your User entity (DB object) into a UserDetailsImpl that Spring can understand.
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole()); // SimpleGrantedAuthority(user.getRole()) → wraps your role ("ROLE_USER", "ROLE_ADMIN") into an object Spring Security understands.
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority) // Collections.singletonList(authority) → puts that one role into a list (because authorities expects a collection).
        );
    }


    // These overridden methods from UserDetails are mandatory for Spring Security.
    // When Spring Security checks credentials, it calls these methods to get the username, password, and roles.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}