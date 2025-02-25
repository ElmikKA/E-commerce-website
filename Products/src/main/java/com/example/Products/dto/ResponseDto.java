package com.example.Products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto {

//    @Schema(description = "Status code")
    private String statusCode;

//    @Schema(description = "Status message")
    private String statusMsg;
}
