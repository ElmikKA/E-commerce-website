package com.example.Products.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponseDto {

//    @Schema(description = "API path")
    private String apiPath;

//    @Schema(description = "Error code")
    private HttpStatus errorCode;

//    @Schema(description = "Error message")
    private String errorMessage;

//    @Schema(description = "Error time")
    private LocalDateTime errorTime;
}
