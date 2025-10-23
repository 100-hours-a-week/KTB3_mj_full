package com.example.restapi_demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/** 게시글 목록 응답 DTO */
@Schema(description = "게시글 목록 응답 데이터")
public class PostListResponse {

    @Schema(description = "게시글 목록 데이터", implementation = PostSummary.class)
    private List<PostSummary> content;

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "1")
    private int page;

    @Schema(description = "페이지당 게시글 수", example = "10")
    private int size;

    @Schema(description = "전체 게시글 개수", example = "125")
    private long totalElements;

    @Schema(description = "전체 페이지 수", example = "13")
    private int totalPages;

    public PostListResponse() {}

    public PostListResponse(List<PostSummary> content, int page, int size) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = content == null ? 0 : content.size();
        this.totalPages = size > 0 ? (int) Math.ceil((double) this.totalElements / size) : 1;
    }

    public PostListResponse(List<PostSummary> content, int page, int size, long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<PostSummary> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
}
