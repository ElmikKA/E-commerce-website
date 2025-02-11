package com.example.Products.service.impl;

import com.example.Products.Entity.Product;
import com.example.Products.dto.ProductDto;
import com.example.Products.exceptions.*;
import com.example.Products.kafka.ProductProducer;
import com.example.Products.mapper.ProductMapper;
import com.example.Products.repository.ProductRepository;
import com.example.Products.security.SecurityUtils;
import com.example.Products.service.IProductService;
import com.example.basedomains.ProductCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final SecurityUtils securityUtils;
    private final ProductProducer productProducer;

    @Override
    public List<ProductDto> fetchProducts() {
        log.info("Fetching all products");
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
    public ProductDto createProduct(ProductDto productDto, MultipartFile file) {
        log.info("Received product DTO: {}", productDto);
        if (!securityUtils.getCurrentUserRole().equals("SELLER")) {
            log.warn("Unauthorized product creation attempt by user: {}", securityUtils.getCurrentUserId());
            throw new UnauthorizedAccessException("Only sellers can create products");
        }

        try {
            Product product = ProductMapper.mapToProduct(productDto, new Product());
            product.setUserId(securityUtils.getCurrentUserId());
            Product savedProduct = productRepository.save(product);

            // If a file is provided, process it
            if (file != null && !file.isEmpty()) {
                // Convert file bytes to a Base64-encoded string
                String base64ImageData = encodeImageToBase64(file);
                ProductCreatedEvent event = new ProductCreatedEvent(savedProduct.getId(), base64ImageData);
                productProducer.sendProductCreatedEvent(event);
            }
            return ProductMapper.mapToProductDto(savedProduct, new ProductDto());
        } catch (IOException e) {
            log.error("Database error while creating product: {}", e.getMessage());
            throw new DatabaseOperationException("Failed to create product due to database error");
        }
    }

    private String encodeImageToBase64(MultipartFile file) throws IOException {
        try {
            byte[] fileBytes = file.getBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            log.error("Error encoding image file to Base64: {}", e.getMessage());
            throw new InvalidInputException("Failed to process the image file");
        }
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId, Authentication authentication) {
        try {
            Product existingProduct = productRepository.findById(productId).orElseThrow(
                    () -> new ResourceNotFoundException("Product", "productId", productId)
            );

            // Verify ownership
            if (!existingProduct.getUserId().equals(authentication.getName())) {
                throw new UnauthorizedAccessException("You are not authorized to update this product");
            }

            updateProductFields(existingProduct, productDto);
            Product updatedProduct = productRepository.save(existingProduct);
            return ProductMapper.mapToProductDto(updatedProduct, new ProductDto());

        } catch (DataAccessException e) {
            log.error("Database error while updating product {}: {}", productId, e.getMessage());
            throw new DatabaseOperationException("Failed to update product due to database error");
        }
    }

    @Override
    public boolean deleteProduct(String productId) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new ResourceNotFoundException("Product", "productId", productId)
            );
            if(!product.getUserId().equals(securityUtils.getCurrentUserId())) {
                throw new UnauthorizedAccessException("You are not authorized to delete this product");
            }

            productRepository.deleteById(productId);
            log.info("Deleted product with id: {}", productId);
            return true;
        } catch (DataAccessException e) {
            log.error("Database error while deleting product {}: {}", productId, e.getMessage());
            throw new DatabaseOperationException("Failed to delete product due to database error");
        }
    }

    private void updateProductFields(Product product, ProductDto productDto) {
        Optional.ofNullable(productDto.getName()).ifPresent(product::setName);
        Optional.ofNullable(productDto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productDto.getPrice()).ifPresent(product::setPrice);
        Optional.of(productDto.getQuantity()).ifPresent(product::setQuantity);
    }
}
