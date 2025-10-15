package com.example.restapi_demo.user.dto;

//비밀번호 변경시 클라이언트에서 전달되는 JSON 데이터를 받는 DTO
public class PasswordChangeRequest {
    private String new_password; //새 비밀번호
    private String new_password_confirm;//새 비밀번호 확인

    public PasswordChangeRequest() {}

    public String getNew_password() { return new_password; }
    public String getNew_password_confirm() { return new_password_confirm; }
}
