package com.example.Users.mapper;

import com.example.Users.dto.UserDto;
import com.example.Users.dto.UserRegisterDto;
import com.example.Users.entity.Roles;
import com.example.Users.entity.User;

public class UserMapper {

    //This will map all the data from accounts to accountsDto
    public static UserDto mapToUsersDto(User user, UserDto userDto) {
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().toString());
        return userDto;
    }

    public static User mapToUsers(UserDto userDto, User user) {
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(Roles.valueOf(userDto.getRole().toUpperCase()));
        return user;
    }
}
