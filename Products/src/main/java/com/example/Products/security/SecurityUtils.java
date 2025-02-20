package com.example.Products.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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
        Jwt jwt = getJwt(authentication);
        log.info("Calls this function");

        String userId = jwt.getClaim("userId");
        if (userId == null || userId.isEmpty()) {
            throw new IllegalStateException("User ID not found in JWT claims");
        }
        return userId;
    }

    public String getCurrentUserRole() {
        Authentication authentication = validateIfUserIsAuthenticated();
        Jwt jwt = getJwt(authentication);

        String role = jwt.getClaim("userRole");
        if (role == null || role.isEmpty()) {
            throw new IllegalStateException("User role not found in JWT claims");
        }
        return role;
    }

    private Jwt getJwt(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        } else if (authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        } else {
            throw new BadCredentialsException("Authentication principal is not a Jwt");
        }
    }

    private Authentication validateIfUserIsAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User is not authenticated");
        }
        return authentication;
    }
}
