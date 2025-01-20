package koodjohvi.buyit.User.Dto;

import jakarta.validation.constraints.Email;
import koodjohvi.buyit.User.Roles;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String name;

    @Email(message = "Email should be Valid")
    private String email;
    private String password;
    private String role;

}
