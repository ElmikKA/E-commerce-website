package com.example.Users.auth;

import com.example.Users.dto.UserRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Authentication and Authorization APIs",
        description = "APIs for user registration, login, and authentication management"
)

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }


    @Operation(
            summary = "Register a new user REST API"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User registered successfully"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid UserRegisterDto request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.register(request));
    }

    @Operation(
            summary = "Login user REST API"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User logged in successfully"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.authenticate(request));
    }
}