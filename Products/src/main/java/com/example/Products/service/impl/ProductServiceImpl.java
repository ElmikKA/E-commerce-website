package com.example.Products.service.impl;

import com.example.Products.Entity.Product;
import com.example.Products.constants.ProductConstants;
import com.example.Products.dto.ProductDto;
import com.example.Products.exceptions.*;
import com.example.Products.kafka.ProductProducer;
import com.example.Products.mapper.ProductMapper;
import com.example.Products.repository.ProductRepository;
import com.example.Products.security.SecurityUtils;
import com.example.Products.service.IProductService;
import com.sharedDto.ProductCreatedEvent;
import com.sharedDto.ProductDeletedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final SecurityUtils securityUtils;
    private final ProductProducer productProducer;
    private final RestTemplate restTemplate;

    @Override
    public List<ProductDto> fetchAllProducts() {
        log.info("Fetching all products");
        try {
            List<Product> products = productRepository.findAll();
            return products.stream().map(product -> {
                ProductDto productDto = ProductMapper.mapToProductDto(product, new ProductDto());

                try {
                    String imagePath = productProducer.requestingProductImages(product.getId());
                    productDto.setImagePath(imagePath);
                } catch (ExecutionException | InterruptedException | TimeoutException e) {
                    throw new RuntimeException(e);
                }

                return productDto;
            }).collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Database error while fetching products: {}", e.getMessage());
            throw new DatabaseOperationException("Failed to retrieve products due to a database error.");
        } catch (Exception e) {
            log.error("Unexpected error while fetching products: {}", e.getMessage());
            throw new InternalServerErrorException("An unexpected error occurred while retrieving the product list.");
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

        Product product = ProductMapper.mapToProduct(productDto, new Product());
        product.setUserId(securityUtils.getCurrentUserId());

        Product savedProduct;
        try {
            savedProduct = productRepository.save(product);
        } catch (Exception e) {
            log.error("Database error while creating product: {}", e.getMessage());
            throw new DatabaseOperationException("Failed to create product due to database error");
        }

        if(file != null && !file.isEmpty()) {
            try{
                validateFile(file);
                String base64ImageData = encodeImageToBase64(file);
                ProductCreatedEvent event = new ProductCreatedEvent(savedProduct.getId(), base64ImageData);
                productProducer.sendProductCreatedEvent(event);
            } catch(Exception e) {
                log.error("Error processing image for product id {}: {}", savedProduct.getId(), e.getMessage());
            }
        }
        return ProductMapper.mapToProductDto(savedProduct, new ProductDto());
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        try {
            Product existingProduct = productRepository.findById(productId).orElseThrow(
                    () -> new ResourceNotFoundException("Product", "productId", productId)
            );

            // Verify ownership
            if (!existingProduct.getUserId().equals(securityUtils.getCurrentUserId())) {
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

    //TODO: Needs to call Media Delete as well with Kafka
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

            ProductDeletedEvent event = new ProductDeletedEvent(product.getId());
            log.info("Sending a message to Media Service");
            productProducer.sendProductDeletedEvent(event);

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

    //Validates the file
    private void validateFile(MultipartFile file) {
        if (!ProductConstants.ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new MediaUploadException("Invalid file type. Only PNG and JPG are allowed.");
        }

        if (file.getSize() > ProductConstants.MAX_FILE_SIZE) {
            throw new MediaUploadException("File size exceeds the 2MB limit.");
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
}
