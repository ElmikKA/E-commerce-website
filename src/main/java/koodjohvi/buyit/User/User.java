package koodjohvi.buyit.User;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String role;
    private String avatar;

    public User(String name, String email, String password, String role, String avatar) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
    }

    //UserDetails implements
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Dont actually need it
    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }
}
