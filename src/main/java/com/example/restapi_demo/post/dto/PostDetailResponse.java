package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "게시글 상세 조회 응답 데이터")
public class PostDetailResponse {

    @Schema(description = "게시글 ID", example = "101")
    private Long post_id;

    @Schema(description = "게시글 제목", example = "오늘의 일기")
    private String title;

    @Schema(description = "작성자 닉네임", example = "박성현")
    private String author;

    @Schema(description = "게시글 본문 내용", example = "오늘은 날씨가 참 좋았습니다.")
    private String content;

    @Schema(description = "이미지 URL 목록", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
    private List<String> images;

    @Schema(description = "좋아요 수", example = "25")
    private int likes;

    @Schema(description = "조회수", example = "134")
    private int views;

    @Schema(description = "댓글 개수", example = "3")
    private int comments_count;

    @Schema(description = "현재 요청자가 작성자인지 여부", example = "true")
    private boolean is_author;

    @Schema(description = "게시글 생성 시각", example = "2025-10-19T14:35:00")
    private LocalDateTime created_at;

    @Schema(description = "게시글 수정 시각", example = "2025-10-19T15:00:00")
    private LocalDateTime updated_at;

    public PostDetailResponse() {}

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
