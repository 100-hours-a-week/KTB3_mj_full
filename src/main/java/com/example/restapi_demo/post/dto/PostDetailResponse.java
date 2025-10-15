package com.example.restapi_demo.post.dto;

import java.time.LocalDateTime;
import java.util.List;

//게시글 상세 조회 응답 DTO
public class PostDetailResponse {
    private Long post_id;
    private String title;
    private String author;
    private String content;
    private List<String> images;
    private int likes;
    private int views;
    private int comments_count;
    private boolean is_author;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public PostDetailResponse(Long post_id, String title, String author, String content, List<String> images,
                              int likes, int views, int comments_count, boolean is_author,
                              LocalDateTime created_at, LocalDateTime updated_at) {
        this.post_id = post_id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.images = images;
        this.likes = likes;
        this.views = views;
        this.comments_count = comments_count;
        this.is_author = is_author;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getter
    public Long getPost_id() { return post_id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public List<String> getImages() { return images; }
    public int getLikes() { return likes; }
    public int getViews() { return views; }
    public int getComments_count() { return comments_count; }
    public boolean isIs_author() { return is_author; } // JSON 호환용 네이밍
    public LocalDateTime getCreated_at() { return created_at; }
    public LocalDateTime getUpdated_at() { return updated_at; }
}
