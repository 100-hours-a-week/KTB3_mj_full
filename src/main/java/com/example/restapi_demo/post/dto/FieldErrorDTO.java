package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "요청 데이터 유효성 검증 실패 시 반환되는 필드 오류 정보")
public class FieldErrorDTO {

    @Schema(description = "오류가 발생한 필드명", example = "title")
    private String field;

    @Schema(description = "오류 사유", example = "too_long")
    private String reason;

    public FieldErrorDTO() {}

    public FieldErrorDTO(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }

    public String getField() { return field; }
    public String getReason() { return reason; }
}
