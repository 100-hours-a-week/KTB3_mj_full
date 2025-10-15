package com.example.restapi_demo.post.dto;

import java.time.LocalDateTime;

// 댓글 작성 응답 데이터 구조 정의
public class CommentResponse {
    private Long comment_id;
    private String author;
    private String content;
    private LocalDateTime created_at;

    public CommentResponse(Long comment_id, String author, String content, LocalDateTime created_at) {
        this.comment_id = comment_id;
        this.author = author;
        this.content = content;
        this.created_at = created_at;
    }

    public Long getComment_id() { return comment_id; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getCreated_at() { return created_at; }
}
