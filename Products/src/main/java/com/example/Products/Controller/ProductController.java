package com.example.Products.Controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/products", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProductController {

    @GetMapping("products")
    public ResponseEntity<>
}
