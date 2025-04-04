package com.example.Users.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component // Make this a Spring Bean so Feign discovers it
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Try to get the current HTTP request attributes
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // Get the Authorization header from the incoming request
            String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

            // Check if the header exists and starts with "Bearer "
            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_TYPE)) {
                // Add the existing Authorization header to the outgoing Feign request
                requestTemplate.header(AUTHORIZATION_HEADER, authorizationHeader);
            }
        }
    }
}
