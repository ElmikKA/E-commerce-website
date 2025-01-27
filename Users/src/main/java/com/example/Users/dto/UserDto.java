package com.example.Users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Unwrapped;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name= "User",
        description = "Schema to hold User Information"
)
public class UserDto {

    @Schema(
            name= "id",
            description = "User id"
    )
    private String id;

    @Schema(
            name= "name",
            description = "User name"
    )
    @NotEmpty(message = "Name should not be empty or null")
    @Size(min = 5, max = 30, message = "Name should be between 5 and 30 characters")
    private String name;

    @Schema(
            name= "email",
            description = "User email"
    )
    @NotEmpty(message = "Email should not be empty or null")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(
            name= "role",
            description = "User role"
    )
    @NotEmpty(message = "Role should not be empty or null")
    private String role;
}