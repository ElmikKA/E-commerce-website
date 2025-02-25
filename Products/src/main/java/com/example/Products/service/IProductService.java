package com.example.Products.service;

import com.example.Products.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    List<ProductDto> fetchAllProducts();

    List<ProductDto> fetchProductByUserId(String userId);

    ProductDto createProduct(ProductDto productDto, MultipartFile file);

    ProductDto updateProduct(ProductDto productDto, String productId);

    boolean deleteProduct(String productId);
}
