package com.example.Products.constants;


import java.util.List;

public class ProductConstants {

    private ProductConstants(){
        //No one can create object of this class
    }

    public static final String TOPIC_CREATED = "product-created";
    public static final String TOPIC_DELETED = "product-deleted";
    public static final String TOPIC_GETTING_IMAGE = "product-image-request";
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    public static final List<String> ALLOWED_FILE_TYPES = List.of("image/png", "image/jpg");

    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "Product created successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Product update operation failed. Please try again.";
    public static final String  MESSAGE_417_DELETE= "Product delete operation failed. Please try again.";
}
