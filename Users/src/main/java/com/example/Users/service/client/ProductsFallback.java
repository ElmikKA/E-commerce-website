package com.example.Users.service.client;

import com.example.Users.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductsFallback implements ProductsFeignClient{
    @Override
    public ResponseEntity<List<ProductDto>> fetchProductDetails(String correlationId, String userId) {
        return null;
    }
}
