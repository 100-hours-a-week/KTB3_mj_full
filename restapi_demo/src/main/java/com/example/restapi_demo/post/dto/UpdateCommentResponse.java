package com.example.restapi_demo.post.dto;

//댓글 수정 응답 DTO
public class UpdateCommentResponse {
    private Long comment_id;
    private String content;

    public UpdateCommentResponse(Long comment_id, String content) {
        this.comment_id = comment_id;
        this.content = content;
    }

    public Long getComment_id() { return comment_id; }
    public String getContent() { return content; }
}
