package com.example.restapi_demo.auth.dto;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {}

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
