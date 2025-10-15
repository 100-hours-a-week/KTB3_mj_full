package com.example.restapi_demo.post.dto;

//댓글 수정 요청 DTO
public class UpdateCommentRequest {
    private String content;

    public UpdateCommentRequest() {}
    public UpdateCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() { return content; }
}
