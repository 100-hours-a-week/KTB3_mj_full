package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시글 생성 성공 시 data 스키마")
public class PostCreateResponse {
    @Schema(description = "게시글 ID", example = "123")
    private Long post_id;
    @Schema(description = "제목", example = "제목")
    private String title;
    @Schema(description = "작성자", example = "나")
    private String author;
    @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/post/img1.jpg")
    private String image;
    @Schema(description = "작성 시각", example = "2025-09-27T12:00:00")
    private LocalDateTime created_at;

    public PostCreateResponse() {}

    public PostCreateResponse(Long post_id, String title, String author, String image, LocalDateTime created_at) {
        this.post_id = post_id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.created_at = created_at;
    }

    public Long getPost_id() { return post_id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getImage() { return image; }
    public LocalDateTime getCreated_at() { return created_at; }
}
