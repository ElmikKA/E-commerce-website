package koodjohvi.buyit.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String id;
    private String name;
    private String email;
    private String role;
}
