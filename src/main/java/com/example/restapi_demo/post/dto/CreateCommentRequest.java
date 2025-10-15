package com.example.restapi_demo.post.dto;

/** 댓글 작성 요청 바디 */
public class CreateCommentRequest {
    private String content;

    public CreateCommentRequest() {}
    public CreateCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() { return content; }
}
