package com.example.Products.mapper;

import com.example.Products.Entity.Product;
import com.example.Products.dto.ProductDto;

public class ProductMapper {
    public static ProductDto mapToProductDto(Product product, ProductDto productDto) {
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setUserId(product.getUserId());
        return productDto;
    }

    public static Product mapToProduct(ProductDto productDto, Product product) {
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setUserId(productDto.getUserId());
        return product;
    }
}
