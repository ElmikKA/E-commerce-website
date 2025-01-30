package com.example.Media.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@Schema(
//        name = "Respone",
//        description = "Respone"
//)
public class ResponseDto {

//    @Schema(description = "Status code")
    private String statusCode;

//    @Schema(description = "Status message")
    private String statusMsg;
}
