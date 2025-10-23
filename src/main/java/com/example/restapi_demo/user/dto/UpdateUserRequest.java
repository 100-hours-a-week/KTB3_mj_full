package com.example.restapi_demo.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 수정 요청 바디")
public class UpdateUserRequest {
    @Schema(description = "닉네임 (필수, 최대 20자)", example = "새로운닉네임")
    private String nickname;          // 닉네임 필수
    @Schema(description = "프로필 이미지 URL (선택)", example = "https://cdn.example.com/avatar.jpg")
    private String profile_image;     // 프사 선택

    public UpdateUserRequest() {}

    public String getNickname() { return nickname; }
    public String getProfile_image() { return profile_image; }
}
