package com.example.restapi_demo.post.dto;

import java.time.LocalDateTime;

//게시글 목록 응답 DTO
public class PostSummary {
    private Long postId;    // 게시글 ID
    private String title;   // 제목
    private String author;  // 작성자
    private int likes;      // 좋아요 수
    private int comments;   // 댓글 수
    private int views;      // 조회수
    private LocalDateTime createdAt; // 작성일

    public PostSummary(Long postId, String title, String author,
                       int likes, int comments, int views, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.likes = likes;
        this.comments = comments;
        this.views = views;
        this.createdAt = createdAt;
    }

    // Getter
    public Long getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getLikes() { return likes; }
    public int getComments() { return comments; }
    public int getViews() { return views; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
