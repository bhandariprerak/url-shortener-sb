package com.url.shortener.security;

import com.url.shortener.security.jwt.JwtAuthenticationFilter;
import com.url.shortener.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//* Custom Security Configuration for our application Defines the security rules for handling HTTP requests.
//Configure JwtAuthenticationFilter in filter chain
//* Spring Security recognizes it as a filter that will only be executed once per request.
//* By default, Spring Security doesn't automatically include your custom filter (JwtAuthenticationFilter) in the filter chain unless you explicitly add it.
//Configure DaoAuthenticationProvider
//Sets up how authentication is handled by Spring Security
//* Bean for AuthenticationManager and PasswordEncoder


@Configuration // @Configuration → Marks this class as a configuration file for Spring.
@EnableWebSecurity // @EnableWebSecurity → Turns on Spring Security for the app.
@EnableMethodSecurity // @EnableMethodSecurity → Allows you to use method-level security (@PreAuthorize, @Secured, etc.).
@AllArgsConstructor
public class WebSecurityConfig {
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() { // JwtAuthenticationFilter → Your custom filter that validates JWT tokens in each request.
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // PasswordEncoder → Uses BCryptPasswordEncoder for hashing passwords before saving them to DB.
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager is the Spring Security component that performs authentication (checks username/password).
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    DaoAuthenticationProvider → Tells Spring how to authenticate users:
//            •	Looks up users via your UserDetailsServiceImpl.
//            •	Validates passwords using the PasswordEncoder.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

//    .csrf(AbstractHttpConfigurer::disable) → Disables CSRF protection (common for REST APIs).
//            •	.authorizeHttpRequests(...) → Defines which endpoints require login:
//            •	/api/auth/** → open (signup, login).
// •	/api/urls/** → only authenticated users can access.
// •	/{shortUrl} → open (so anyone with the short link can be redirected).
// •	.anyRequest().authenticated() → everything else needs authentication.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/urls/**").authenticated()
                        .requestMatchers("/{shortUrl}").permitAll()
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Adds jwtAuthenticationFilter custom security filter before in-built filter of UsernamePasswordAuthenticationFilter
        return http.build();
    }

}