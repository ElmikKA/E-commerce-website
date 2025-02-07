package com.example.Products.Controller;

import com.example.Products.dto.ProductDto;
import com.example.Products.service.IProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(productService.fetchProducts());
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
        log.info("Creating new product");
        return ResponseEntity.ok(productService.createProduct(productDto));
    }


}
