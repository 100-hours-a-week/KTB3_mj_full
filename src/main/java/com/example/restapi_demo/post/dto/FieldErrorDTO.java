package com.example.restapi_demo.post.dto;

//오류 응답 구조 정의
public class FieldErrorDTO {
    private String field;
    private String reason;

    public FieldErrorDTO(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }

    public String getField() { return field; }
    public String getReason() { return reason; }
}
