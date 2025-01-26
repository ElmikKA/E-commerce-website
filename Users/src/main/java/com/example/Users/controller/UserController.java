package com.example.Users.controller;

import com.example.Users.dto.UserDto;
import com.example.Users.dto.UserRegisterDto;
import com.example.Users.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<UserDto>> fetchUser() {
        List<UserDto> users = userService.fetchUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }
}
