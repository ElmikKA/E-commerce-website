package com.example.Products.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Allow GET requests to /products (listing products)
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        // Require "SELLER" role for POST requests to /products (creating products)
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("SELLER")
                        // Any other requests can be authenticated (adjust as needed)
                        .anyRequest().authenticated() // Or permitAll() if you really want other endpoints open
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
