package com.example.restapi_demo.post.dto;

import java.util.List;

//게시글 목록 응답 DTO
public class PostListResponse {
    private List<PostSummary> content;  // 게시글 목록
    private int page;                   // 현재 페이지 번호
    private int size;                   // 페이지당 게시글 수
    private long totalElements;         // 전체 게시글 개수
    private int totalPages;             // 전체 페이지 수

    // 생성자
    public PostListResponse(List<PostSummary> content, int page, int size) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = content == null ? 0 : content.size();
        this.totalPages = size > 0 ? (int) Math.ceil((double) this.totalElements / size) : 1;
    }

    // 전체 필드 지정 생성자
    public PostListResponse(List<PostSummary> content, int page, int size, long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    // Getter
    public List<PostSummary> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
}
