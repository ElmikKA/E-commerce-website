package com.example.Users.service;

import com.example.Users.dto.UserDto;
import com.example.Users.dto.UserRegisterDto;

import java.util.List;

public interface IUsersService {

    List<UserDto> fetchUsers();
}
