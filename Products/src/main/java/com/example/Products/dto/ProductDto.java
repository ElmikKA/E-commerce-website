package com.example.Products.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private Double price;
    private int quality;
    private String userId;
}
