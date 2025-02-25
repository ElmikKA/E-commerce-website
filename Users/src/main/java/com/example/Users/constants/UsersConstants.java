package com.example.Users.constants;

public class UsersConstants {
    private UsersConstants(){
        //No one can create object of this class
    }

    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "User created successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again.";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again.";
    public static final String SECRET_KEY = "0f4a7d3b29a6f4c9e5b8c9e3b519f4f2d3a1e6a94f2b5e1b3f1a1e6c4b3f5a6e";
    public static final long TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 24 hours
}
