package com.example.Products.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityUtils {

    public String getCurrentUserId() {
        Authentication authentication = validateIfUserIsAuthenticated();

        log.info("Calls this function");

        Jwt jwt;
        if (authentication instanceof JwtAuthenticationToken) {
            jwt = ((JwtAuthenticationToken) authentication).getToken();
        } else if (authentication.getPrincipal() instanceof Jwt) {
            jwt = (Jwt) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("Authentication principal is not a Jwt");
        }

        String userId = jwt.getClaim("userId");
        if (userId == null || userId.isEmpty()) {
            throw new IllegalStateException("User ID not found in JWT claims");
        }
        return userId;
    }

    public String getCurrentUserRole() {
        Authentication authentication = validateIfUserIsAuthenticated();

        Jwt jwt;
        if (authentication instanceof JwtAuthenticationToken) {
            jwt = ((JwtAuthenticationToken) authentication).getToken();
        } else if (authentication.getPrincipal() instanceof Jwt) {
            jwt = (Jwt) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("Authentication principal is not a Jwt");
        }

        String role = jwt.getClaim("userRole");
        if (role == null || role.isEmpty()) {
            throw new IllegalStateException("User role not found in JWT claims");
        }
        return role;
    }

    private Authentication validateIfUserIsAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        return authentication;
    }
}
