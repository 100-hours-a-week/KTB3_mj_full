package com.example.restapi_demo.post.model;

import java.time.LocalDateTime;

public class Comment {
    private Long commentId;//댓글 고유 식별자
    private Long postId;//댓글이 달린 게시글의 ID
    private Long authorId;        // 작성자 식별용
    private String authorName;    // 응답용 표시 이름 (예: "나")
    private String content;//댓글 내용
    private LocalDateTime createdAt;//댓글 생성 시각

    //생성자 선언
    public Comment(Long commentId, Long postId, Long authorId, String authorName, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getCommentId() { return commentId; }
    public Long getPostId() { return postId; }
    public Long getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    //댓글 내용 수정 메소드
    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
