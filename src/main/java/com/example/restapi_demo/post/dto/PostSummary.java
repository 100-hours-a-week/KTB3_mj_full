package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/** 게시글 목록 개별 요약 정보 DTO */
@Schema(description = "게시글 목록의 개별 게시글 요약 정보")
public class PostSummary {

    @Schema(description = "게시글 ID", example = "101")
    private Long postId;

    @Schema(description = "게시글 제목", example = "오늘의 일기")
    private String title;

    @Schema(description = "작성자 이름", example = "박성현")
    private String author;

    @Schema(description = "좋아요 수", example = "27")
    private int likes;

    @Schema(description = "댓글 수", example = "5")
    private int comments;

    @Schema(description = "조회수", example = "342")
    private int views;

    @Schema(description = "게시글 작성 시각", example = "2025-10-19T15:20:00")
    private LocalDateTime createdAt;

    public PostSummary() {}

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

    public Long getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getLikes() { return likes; }
    public int getComments() { return comments; }
    public int getViews() { return views; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
