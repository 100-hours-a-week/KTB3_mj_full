package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/** 게시글 수정 응답 DTO */
@Schema(description = "게시글 수정 결과 응답 데이터")
public class PostUpdateResponse {

    @Schema(description = "게시글 ID", example = "101")
    private Long post_id;

    @Schema(description = "수정된 게시글 제목", example = "오늘의 일기 (수정됨)")
    private String title;

    @Schema(description = "수정된 게시글 내용", example = "내용이 일부 수정되었습니다.")
    private String content;

    @Schema(description = "게시글 대표 이미지 URL", example = "https://example.com/updated-image.jpg", nullable = true)
    private String image;

    @Schema(description = "게시글 수정 시각", example = "2025-10-19T22:15:00")
    private LocalDateTime updated_at;

    public PostUpdateResponse() {}

    public PostUpdateResponse(Long post_id, String title, String content, String image, LocalDateTime updated_at) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.updated_at = updated_at;
    }

    public Long getPost_id() { return post_id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImage() { return image; }
    public LocalDateTime getUpdated_at() { return updated_at; }
}
