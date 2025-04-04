package com.example.Users.service;

import com.example.Users.dto.UserDetailsDto;
import com.example.Users.dto.UserDto;

import java.util.List;

public interface IUsersService {

    List<UserDto> fetchUsers();

    UserDto fetchUserById(String id);

    boolean updatedUser(UserDto userDto);

    boolean deletedUser(String id);

}
