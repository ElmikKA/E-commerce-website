package com.example.Users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name= "UserRegisterDto", description = "DTO for registering a new user"
)
public class UserRegisterDto {

    @NotEmpty
    @Schema(
            description = "Name should not be empty or null", example = "John Doe"
    )
    private String name;

    @Schema(
            description = "Email should be Valid", example = "hello@example.com"
    )
    @Email(message = "Email should be Valid")
    @NotEmpty(message = "Email should not be empty or null")
    private String email;

    @Schema(
            description = "Password should be at least 5 characters long", example = "123456"
    )
    @NotEmpty(message = "Password should not be empty or null")
    @Size(min = 5, message = "Password should be at least 5 characters long")
    private String password;

    @Schema(
            description ="The Role must be CLIENT or SELLER", example = "SELLER"
    )
    @NotEmpty(message = "Role should not be empty or null")
    private String role;

}
