package com.example.Products.service.impl;

import com.example.Products.dto.ProductDto;
import com.example.Products.service.IProductService;

import java.util.List;

public class ProductServiceImpl implements IProductService {
    @Override
    public List<ProductDto> fetchProducts() {
        return List.of();
    }

    @Override
    public ProductDto fetchProductById(String id) {
        return null;
    }

    @Override
    public ProductDto fetchProductByUserId(String userId) {
        return null;
    }

    @Override
    public boolean updateProduct(ProductDto productDto) {
        return false;
    }

    @Override
    public boolean deleteProduct(String id) {
        return false;
    }
}
