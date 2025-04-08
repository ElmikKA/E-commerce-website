package com.example.Users.controller;

import com.example.Users.dto.UserDetailsDto;
import com.example.Users.service.IUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "CRUD REST APIs fo Users",
        description = "CRUD REST APIs for Users to create READ, UPDATE, GET, DELETE"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserDetailsController {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsController.class);
    IUserDetailsService userDetailsService;

    @Operation(
            summary = "Fetch Users details"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status OK"
    )
    @GetMapping("/fetchUserDetails")
    public ResponseEntity<UserDetailsDto> fetchUserDetails(
            @RequestHeader("buyit-correlation-id")
            String correlationId,
            @RequestParam String userId) {
        log.debug("fetchUserDetails method start");
        UserDetailsDto userDetailsDto = userDetailsService.fetchUserDetails(userId, correlationId);
        log.debug("fetchUserDetails method end");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDetailsDto);
    }
}
