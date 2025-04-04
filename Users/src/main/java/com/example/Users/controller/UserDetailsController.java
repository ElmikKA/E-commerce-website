package com.example.Users.controller;

import com.example.Users.dto.UserDetailsDto;
import com.example.Users.service.IUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "CRUD REST APIs fo Users",
        description = "CRUD REST APIs for Users to create READ, UPDATE, GET, DELETE"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserDetailsController {

    IUserDetailsService userDetailsService;

    @Operation(
            summary = "Fetch Users details"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status OK"
    )
    @GetMapping("/fetchUserDetails")
    public ResponseEntity<UserDetailsDto> fetchUserDetails(@RequestParam String userId) {
        UserDetailsDto userDetailsDto = userDetailsService.fetchUserDetails(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDetailsDto);
    }
}
