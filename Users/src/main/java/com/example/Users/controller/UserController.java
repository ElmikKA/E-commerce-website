package com.example.Users.controller;

import com.example.Users.constants.UsersConstants;
import com.example.Users.dto.UserDto;
import com.example.Users.service.impl.UserServiceImpl;
import com.sharedDto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs fo Users",
        description = "CRUD REST APIs for Users to create READ, UPDATE, GET, DELETE"
)
@RestController
@RequestMapping(path = "/api/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserServiceImpl userService;


    @Operation(
            summary = "Fetch all Users from database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status OK"
    )
    @GetMapping("/fetchAll")
    public ResponseEntity<List<UserDto>> fetchUser(@RequestHeader("buyit-correlation-id")
                                                       String correlationId) {
        log.debug("buyit-correlation-id found in fetchUsers() {}: ", correlationId);
        List<UserDto> users = userService.fetchUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }


    @Operation(
        summary = "Fetch User by id from database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status OK"
    )
    @GetMapping("/fetchById/{id}")
    public ResponseEntity<UserDto> fetchUserById(@RequestHeader("buyit-correlation-id")
                                                     String correlationId,
                                                 @PathVariable String id) {
        log.debug("buyit-correlation-id found fetchUsersById() {}: ", correlationId);
        UserDto user = userService.fetchUserById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }


    @Operation(
            summary = "Update User details REST API"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status Internal Server Error"
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateUserDetails(@RequestHeader("buyit-correlation-id")
                                                             String correlationId,
                                                         @Valid @RequestBody UserDto userDto) {
        log.debug("buyit-correlation-id found updatedUserDetails() {}: ", correlationId);
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

    @Operation(
            summary = "Delete User details REST API"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status Internal Server Error"
            )
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@RequestHeader("buyit-correlation-id")
                                                      String correlationId,
                                                  @PathVariable String id) {
        log.debug("buyit-correlation-id found deleteUser() {}: ", correlationId);
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
