package com.example.Users.auth;

import com.example.Users.dto.UserRegisterDto;
import com.example.Users.entity.Roles;
import com.example.Users.entity.User;
import com.example.Users.jwt.JwtUtils;
import com.example.Users.repository.UserRepository;
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

    public AuthenticationResponse register(UserRegisterDto request) {
        Roles roles;
        try{
            roles = Roles.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + request.getRole());
        }
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roles)
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
