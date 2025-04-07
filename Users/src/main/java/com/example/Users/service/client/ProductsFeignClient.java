package com.example.Users.service.client;

import com.example.Users.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name ="products", fallback = ProductsFallback.class)
public interface ProductsFeignClient {

    @GetMapping(value = "/api/products/fetchByUserId", consumes = "application/json")
    public ResponseEntity<List<ProductDto>> fetchProductDetails(@RequestHeader("buyit-correlation-id") String correlationId,  @RequestParam String userId);

}
