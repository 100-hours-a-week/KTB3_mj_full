package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** 게시글 수정 요청 DTO */
@Schema(description = "게시글 수정 요청 데이터")
public class PostUpdateRequest {

    @Schema(description = "게시글 제목", example = "오늘의 일기 수정본")
    private String title;

    @Schema(description = "게시글 본문 내용", example = "오늘은 날씨가 흐렸지만 기분은 좋았습니다.")
    private String content;

    @Schema(description = "게시글 대표 이미지 URL (선택 입력)", example = "https://example.com/image.jpg", nullable = true)
    private String image;

    public PostUpdateRequest() {}

    public PostUpdateRequest(String title, String content, String image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImage() { return image; }
}
