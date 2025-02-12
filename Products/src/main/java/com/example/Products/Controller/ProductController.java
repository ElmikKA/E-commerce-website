package com.example.Products.Controller;

import com.example.Products.dto.ProductDto;
import com.example.Products.service.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "api/products", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class ProductController {

    private final IProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("Fetching all products");
        return ResponseEntity.ok(productService.fetchAllProducts());
    }

    // Change the mapping to accept multipart/form-data
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // Convert JSON String to ProductDto object
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto productDto = objectMapper.readValue(productJson, ProductDto.class);
            return ResponseEntity.ok(productService.createProduct(productDto, file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid product JSON format");
        }
    }
}
