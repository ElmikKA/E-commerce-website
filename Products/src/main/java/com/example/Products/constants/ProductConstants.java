package com.example.Products.constants;


import java.util.List;

public class ProductConstants {

    private ProductConstants(){
        //No one can create object of this class
    }

    public static final String TOPIC = "product-created";
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    public static final List<String> ALLOWED_FILE_TYPES = List.of("image/png", "image/jpg");
}
