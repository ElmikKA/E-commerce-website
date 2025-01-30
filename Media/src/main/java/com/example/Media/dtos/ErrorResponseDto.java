package com.example.Media.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
//@Schema(
//        name = "ErrorResponse",
//        description = "Schema to hold error details"
//)
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
