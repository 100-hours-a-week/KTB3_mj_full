package com.example.restapi_demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

//비밀번호 변경시 클라이언트에서 전달되는 JSON 데이터를 받는 DTO
@Schema(description = "비밀번호 변경 요청 바디")
public class PasswordChangeRequest {
    @Schema(description = "새 비밀번호 (8~20자, 영문/숫자/특수문자 포함)", example = "Abcd1234!")
    private String new_password; //새 비밀번호
    @Schema(description = "새 비밀번호 확인", example = "Abcd1234!")
    private String new_password_confirm;//새 비밀번호 확인

    public PasswordChangeRequest() {}

    public String getNew_password() { return new_password; }
    public String getNew_password_confirm() { return new_password_confirm; }
}
