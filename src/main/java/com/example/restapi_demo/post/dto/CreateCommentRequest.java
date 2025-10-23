package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** 댓글 작성 요청 바디 */
@Schema(description = "댓글 작성 요청 바디")
public class CreateCommentRequest {

    @Schema(
            description = "댓글 내용",
            example = "댓글입니닷",
            required = true
    )
    private String content;

    public CreateCommentRequest() {}
    public CreateCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; } // 선택(있으면 직렬화/문서화에 유리)
}
