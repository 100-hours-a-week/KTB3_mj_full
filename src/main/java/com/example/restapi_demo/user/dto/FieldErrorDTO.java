package com.example.restapi_demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

//검증실패시 필드와 사유를 알려주는 부분
@Schema(description = "요청 필드 오류 항목")
public class FieldErrorDTO {
    @Schema(description = "오류가 발생한 필드명", example = "nickname")
    private String field;//오류가 발생한 필드 이름
    @Schema(description = "오류 사유 코드", example = "too_long")
    private String reason;//오류 사유

    public FieldErrorDTO(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }

    public String getField() { return field; }
    public String getReason() { return reason; }
}
