package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좋아요 수 응답")
public class LikeCountDTO {
    @Schema(description = "현재 좋아요 수", example = "123")
    private int likes;

    public LikeCountDTO() {}
    public LikeCountDTO(int likes) { this.likes = likes; }

    public int getLikes() { return likes; }
}
