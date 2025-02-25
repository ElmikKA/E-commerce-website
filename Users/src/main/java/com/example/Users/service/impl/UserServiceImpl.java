package com.example.Users.service.impl;

import com.example.Users.dto.UserDto;
import com.example.Users.dto.UserRegisterDto;
import com.example.Users.entity.User;
import com.example.Users.exceptions.UserNotFoundException;
import com.example.Users.mapper.UserMapper;
import com.example.Users.repository.UserRepository;
import com.example.Users.service.IUsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements IUsersService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> fetchUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) return List.of();
        log.info("Fetching user all users");
        return users.stream().map(user -> UserMapper.mapToUsersDto(user, new UserDto())).toList();
    }

    @Override
    public UserDto fetchUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User", "id", id)
        );
        log.info("Fetching user with id: {}", id);
        return UserMapper.mapToUsersDto(user, new UserDto());
    }

    @Override
    public boolean updatedUser(UserDto userDto) {
        boolean isUpdated = false;
        if(userDto.getId() != null) {
            User user = userRepository.findById(userDto.getId()).orElseThrow(
                    () -> new UserNotFoundException("User", "email", userDto.getId())
            );
            UserMapper.mapToUsers(userDto, user);
            userRepository.save(user);
            isUpdated = true;
            log.info("Updated user with email: {}", userDto.getEmail());
        }
        return isUpdated;
    }

    @Override
    public boolean deletedUser(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User", "id", id)
        );
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
        return true;
    }
}
