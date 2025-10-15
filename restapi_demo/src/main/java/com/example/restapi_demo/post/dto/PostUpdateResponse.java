package com.example.restapi_demo.post.dto;

import java.time.LocalDateTime;

//게시글 수정 응답 DTO
public class PostUpdateResponse {
    private Long post_id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime updated_at;

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
