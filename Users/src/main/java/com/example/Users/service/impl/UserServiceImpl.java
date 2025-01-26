package com.example.Users.service.impl;

import com.example.Users.dto.UserDto;
import com.example.Users.dto.UserRegisterDto;
import com.example.Users.entity.User;
import com.example.Users.mapper.UserMapper;
import com.example.Users.repository.UserRepository;
import com.example.Users.service.IUsersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUsersService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> fetchUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) return List.of();
        return users.stream().map(user -> UserMapper.mapToUsersDto(user, new UserDto())).toList();
    }
}
