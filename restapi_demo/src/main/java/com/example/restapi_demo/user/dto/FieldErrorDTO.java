package com.example.restapi_demo.user.dto;

//검증실패시 필드와 사유를 알려주는 부분
public class FieldErrorDTO {
    private String field;//오류가 발생한 필드 이름
    private String reason;//오류 사유

    public FieldErrorDTO(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }

    public String getField() { return field; }
    public String getReason() { return reason; }
}
