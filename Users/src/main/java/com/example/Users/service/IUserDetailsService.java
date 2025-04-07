package com.example.Users.service;

import com.example.Users.dto.UserDetailsDto;

public interface IUserDetailsService {
    UserDetailsDto fetchUserDetails(String userId, String correlationId);
}
