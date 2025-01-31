package com.example.Products.service;

import com.example.Products.dto.ProductDto;

import java.util.List;

public interface IProductService {
    List<ProductDto> fetchProducts();

    ProductDto fetchProductById(String id);

    List<ProductDto> fetchProductByUserId(String userId);

    ProductDto updateProduct(ProductDto productDto, String productId);

    boolean deleteProduct(String id);
}
