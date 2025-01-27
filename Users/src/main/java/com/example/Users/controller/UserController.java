package com.example.Users.controller;

import com.example.Users.constants.UsersConstants;
import com.example.Users.dto.ResponseDto;
import com.example.Users.dto.UserDto;
import com.example.Users.dto.UserRegisterDto;
import com.example.Users.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/fetchById/{id}")
    public ResponseEntity<UserDto> fetchUserById(@PathVariable String id) {
        UserDto user = userService.fetchUserById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateUserDetails(@Valid @RequestBody UserDto userDto) {
        boolean isUpdated = userService.updatedUser(userDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(UsersConstants.STATUS_200, UsersConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(UsersConstants.STATUS_417, UsersConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deletedUser(id);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(UsersConstants.STATUS_200, UsersConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(UsersConstants.STATUS_417, UsersConstants.MESSAGE_417_DELETE));
        }
    }
}
