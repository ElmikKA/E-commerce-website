package com.example.Users.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    private String name;

    @Email(message = "Email should be Valid")
    private String email;

    private String password;

    private String role;

}
