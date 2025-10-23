package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** 댓글 수정 요청 DTO */
@Schema(description = "댓글 수정 요청 데이터")
public class UpdateCommentRequest {

    @Schema(description = "수정할 댓글 내용", example = "내용을 조금 고쳤어요!")
    private String content;

    public UpdateCommentRequest() {}

    public UpdateCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() { return content; }
}
