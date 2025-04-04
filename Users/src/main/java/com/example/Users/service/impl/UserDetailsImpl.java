package com.example.Users.service.impl;

import com.example.Users.dto.ProductDto;
import com.example.Users.dto.UserDetailsDto;
import com.example.Users.entity.User;
import com.example.Users.exceptions.InternalServerErrorException;
import com.example.Users.exceptions.UserNotFoundException;
import com.example.Users.mapper.UserMapper;
import com.example.Users.repository.UserRepository;
import com.example.Users.service.IUserDetailsService;
import com.example.Users.service.client.ProductsFeignClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsImpl implements IUserDetailsService {

    private UserRepository userRepository;
    private ProductsFeignClient productsFeignClient;

    @Override
    public UserDetailsDto fetchUserDetails(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User", "id", userId)
        );

        log.info("Fetching user details with id: {}", userId);

        UserDetailsDto userDetailsDto = UserMapper.mapToUsersDetailDto(user, new UserDetailsDto());
        try{
            log.info("It goes inside of a try block");
            ResponseEntity<List<ProductDto>> productsDtoResponseEntity = productsFeignClient.fetchProductDetails(userId);
            if (productsDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("return 200 and is in the fetch status");
                List<ProductDto> productList = productsDtoResponseEntity.getBody();
                userDetailsDto.setProductDto(productList);
            } else {
                log.error("Received non-successful status {} from products service for user id: {}. Body: {}",
                    productsDtoResponseEntity.getStatusCode(),
                    userId,
                    productsDtoResponseEntity.getBody()); // Be cautious logging the body, might contain error details or be large
            }
        } catch (Exception e) {
            log.error("Unexpected error while fetching user details: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while retrieving user details with id: " + userId);
        }

        return userDetailsDto;
    }
}
