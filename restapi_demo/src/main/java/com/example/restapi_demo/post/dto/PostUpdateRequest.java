package com.example.restapi_demo.post.dto;

//게시글 수정 요청 DTO
public class PostUpdateRequest {
    private String title;
    private String content;
    private String image; // 선택

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
