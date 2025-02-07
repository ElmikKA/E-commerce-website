package com.example.Products.service;

import com.example.Products.dto.ProductDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IProductService {
    List<ProductDto> fetchProducts();

    ProductDto fetchProductById(String id);

    List<ProductDto> fetchProductByUserId(String userId);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, String productId, Authentication authentication);

    boolean deleteProduct(String productId);
}
