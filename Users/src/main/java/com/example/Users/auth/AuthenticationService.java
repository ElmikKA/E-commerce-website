package com.example.Users.auth;

import com.example.Users.dto.UserRegisterDto;
import com.example.Users.entity.Roles;
import com.example.Users.entity.User;
import com.example.Users.exceptions.UserAlreadyExistsException;
import com.example.Users.jwt.JwtUtils;
import com.example.Users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRegisterDto request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(
                user -> {
                    throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
                }
        );
        Roles roles = Arrays.stream(Roles.values())
                .filter(role -> role.name().equalsIgnoreCase(request.getRole()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The Role must be CLIENT or SELLER, but you but: " + request.getRole()));

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roles)
                .build();
        log.info("Registering new user with email: {}", request.getEmail());

        userRepository.save(user);
        var jwtToken = jwtUtil.generateToken(user, user.getId(), user.getRole());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        log.info("Email: {}", request.getEmail());
        log.info("Password: {}", request.getPassword());
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtUtil.generateToken(user, user.getId(), user.getRole());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
