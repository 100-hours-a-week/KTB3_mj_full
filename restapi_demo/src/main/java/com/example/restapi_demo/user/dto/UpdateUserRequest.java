package com.example.restapi_demo.user.dto;

public class UpdateUserRequest {
    private String nickname;          // 닉네임 필수
    private String profile_image;     // 프사 선택

    public UpdateUserRequest() {}

    public String getNickname() { return nickname; }
    public String getProfile_image() { return profile_image; }
}
