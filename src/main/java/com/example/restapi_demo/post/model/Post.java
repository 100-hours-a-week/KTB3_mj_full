package com.example.restapi_demo.post.model;

import java.time.LocalDateTime;

//Post
public class Post {
    private Long postId;       // 게시글 ID
    private String title;      // 제목
    private String author;     // 작성자
    private int likes;         // 좋아요 수
    private int comments;      // 댓글 수
    private int views;         // 조회수
    private LocalDateTime createdAt; // 생성 날짜

    // 생성자: createdAt은 자동으로 현재 시간 지정
    public Post(Long postId, String title, String author, int likes, int comments, int views) {
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.likes = likes;
        this.comments = comments;
        this.views = views;
        this.createdAt = LocalDateTime.now();
    }

    public Long getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getLikes() { return likes; }
    public int getComments() { return comments; }
    public int getViews() { return views; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    //좋아요 수 증가
    public void increaseLikes() {
        this.likes++;
    }

    //좋아요 수 감소
    public void decreaseLikes() {
        if (this.likes > 0) this.likes--;
    }

    // 제목 수정
    public void setTitle(String title) {
        this.title = title;
    }
}
