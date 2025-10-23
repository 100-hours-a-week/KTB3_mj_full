package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** 댓글 수정 응답 DTO */
@Schema(description = "댓글 수정 응답 데이터")
public class UpdateCommentResponse {

    @Schema(description = "댓글 ID", example = "301")
    private Long comment_id;

    @Schema(description = "수정된 댓글 내용", example = "내용을 조금 다듬었습니다!")
    private String content;

    public UpdateCommentResponse(Long comment_id, String content) {
        this.comment_id = comment_id;
        this.content = content;
    }

    public Long getComment_id() { return comment_id; }
    public String getContent() { return content; }
}
