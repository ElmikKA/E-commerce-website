package com.example.Products.service;

import com.example.Products.dto.ProductDto;

import java.util.List;

public interface IProductService {
    List<ProductDto> fetchProducts();

    ProductDto fetchProductById(String id);

    ProductDto fetchProductByUserId(String userId);

    boolean updateProduct(ProductDto productDto);

    boolean deleteProduct(String id);
}
