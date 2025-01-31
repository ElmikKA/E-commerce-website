package com.example.Products.service.impl;

import com.example.Products.Entity.Product;
import com.example.Products.dto.ProductDto;
import com.example.Products.exceptions.DatabaseOperationException;
import com.example.Products.exceptions.InternalServerErrorException;
import com.example.Products.exceptions.InvalidInputException;
import com.example.Products.exceptions.ResourceNotFoundException;
import com.example.Products.mapper.ProductMapper;
import com.example.Products.repository.ProductRepository;
import com.example.Products.service.IProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> fetchProducts() {
        try {
            return productRepository.findAll()
                    .stream()
                    .map(product -> ProductMapper.mapToProductDto(product, new ProductDto()))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Database error while fetching products: {}", e.getMessage());
            throw new DatabaseOperationException("Failed to retrieve products due to a database error.");
        } catch (Exception e) {
            log.error("Unexpected error while fetching products: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while retrieving the product list.");
        }
    }

    @Override
    public ProductDto fetchProductById(String productId) {
        validateProductId(productId);
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
            return ProductMapper.mapToProductDto(product, new ProductDto());
        } catch (DataAccessException e) {
            log.error("Database error while fetching product ID {}: {}", productId, e.getMessage());
            throw new DatabaseOperationException("Failed to retrieve product due to database error");
        }
    }

    @Override
    public List<ProductDto> fetchProductByUserId(String userId) {
        validateUserId(userId);
        try {
            List<Product> products = productRepository.findProductByUserId(userId).orElseThrow(
                    () -> new ResourceNotFoundException("Product", "userId", userId)
            );
            return products.stream()
                    .map(product -> ProductMapper.mapToProductDto(product, new ProductDto()))
                    .toList();
        } catch (DataAccessException e) {
            log.error("Database error while fetching products for user {}: {}", userId, e.getMessage());
            throw new DatabaseOperationException("Failed to retrieve products for user");
        }
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        validateProductId(productId);
        validateProductInput(productDto);

        try {
            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

//            // Verify ownership
//            if (!existingProduct.getUserId().equals(securityUtils.getCurrentUserId())) {
//                throw new UnauthorizedAccessException("You are not authorized to update this product");
//            }

            updateProductFields(existingProduct, productDto);
            Product updatedProduct = productRepository.save(existingProduct);
            return ProductMapper.mapToProductDto(updatedProduct, new ProductDto());

        } catch (DataAccessException e) {
            log.error("Database error while updating product {}: {}", productId, e.getMessage());
            throw new DatabaseOperationException("Failed to update product due to database error");
        }
    }

    @Override
    public boolean deleteProduct(String id) {
        return false;
    }

    private void updateProductFields(Product product, ProductDto productDto) {
        Optional.ofNullable(productDto.getName()).ifPresent(product::setName);
        Optional.ofNullable(productDto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productDto.getPrice()).ifPresent(product::setPrice);
        Optional.of(productDto.getQuantity()).ifPresent(product::setQuantity);
    }

    private void validateProductId(String productId) {
        if (!StringUtils.hasText(productId)) {
            throw new InvalidInputException("Product ID cannot be empty");
        }
    }

    private void validateUserId(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new InvalidInputException("User ID cannot be empty");
        }
    }

    private void validateProductInput(ProductDto productDto) {
        if (productDto == null) {
            throw new InvalidInputException("Product data cannot be null");
        }
        if (!StringUtils.hasText(productDto.getName())) {
            throw new InvalidInputException("Product name cannot be empty");
        }
        if (productDto.getPrice() == null || productDto.getPrice() <= 0) {
            throw new InvalidInputException("Invalid product price");
        }
    }


}
