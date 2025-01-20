package koodjohvi.buyit.Auth;

import koodjohvi.buyit.User.Dto.UserRequestDto;
import koodjohvi.buyit.User.User;
import koodjohvi.buyit.User.UserRepository;
import koodjohvi.buyit.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final Logger logger;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRequestDto request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        logger.info("Email: " + request.getEmail());
        logger.info("Password: " + request.getPassword());
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
