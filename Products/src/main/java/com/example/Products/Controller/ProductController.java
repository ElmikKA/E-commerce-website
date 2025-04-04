package com.example.Products.Controller;

import com.example.Products.constants.ProductConstants;
import com.example.Products.dto.ProductDto;
import com.example.Products.service.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharedDto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "api/products", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final IProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestHeader("buyit-correlation-id") String correlationId) {
        log.debug("buyit-correlation-id found getAllProducts() {}", correlationId);
        log.info("Fetching all products");
        return ResponseEntity.ok(productService.fetchAllProducts());
    }

    @GetMapping("/fetchByUserId")
    public ResponseEntity<List<ProductDto>> fetchProductByUserId(@RequestHeader("buyit-correlation-id") String correlationId, @RequestParam String userId) {
        log.debug("buyit-correlation-id found fetchProductByUserId() {}", correlationId);
        List<ProductDto> product = productService.fetchProductByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(product);
    }

    // Change the mapping to accept multipart/form-data
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestHeader("buyit-correlation-id") String correlationId,
            @RequestPart("product") String productJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            log.debug("buyit-correlation.id found createProduct() {}", correlationId);
            //TODO: Convert this back to JSON, you can add JSON format from Front
            // Convert JSON String to ProductDto object
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto productDto = objectMapper.readValue(productJson, ProductDto.class);
            return ResponseEntity.ok(productService.createProduct(productDto, file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid product JSON format");
        }
    }

    @PutMapping("update/{productId}")
    public ResponseEntity<ProductDto> updatedProductInformation(
            @RequestHeader("buyit-correlation-id") String correlationId,
            @RequestBody ProductDto productDto,
            @PathVariable String productId
    ) {
        log.debug("buyit-correlation.id found updatedProductInformation() {}", correlationId);
        ProductDto product = productService.updateProduct(productDto, productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(product);
    }


    @DeleteMapping("delete/{productId}")
    public ResponseEntity<ResponseDto> deleteProduct(
            @RequestHeader("buyit-correlation-id") String correlationId,
            @PathVariable String productId
    ) {
        log.debug("buyit-correlation.id found deleteProduct() {}", correlationId);
        boolean isDeleted = productService.deleteProduct(productId);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ProductConstants.STATUS_200, ProductConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ProductConstants.STATUS_417, ProductConstants.MESSAGE_417_DELETE));
        }
    }
}
