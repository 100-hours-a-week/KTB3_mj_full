package com.example.restapi_demo.post.service;

import com.example.restapi_demo.post.dto.*;
import com.example.restapi_demo.post.model.Post;
import com.example.restapi_demo.post.repository.InMemoryPostRepository;

import java.util.List;
import java.util.stream.Collectors;

//postservice
public class PostService {

    //db 임시로 한다
    private final InMemoryPostRepository repo = new InMemoryPostRepository();

    // 게시글 목록 조히
    public PostListResponse getPosts() {
        List<Post> all = repo.findAll();

        List<PostSummary> content = all.stream()
                .map(p -> new PostSummary(
                        p.getPostId(),
                        p.getTitle(),
                        p.getAuthor(),
                        p.getLikes(),
                        p.getComments(),
                        p.getViews(),
                        p.getCreatedAt()
                ))
                .collect(Collectors.toList());

        int page = 0; //게시글 페이지 현재 페이지 번호
        int size = 10;//한 페이지에 글 10개
        long totalElements = content.size(); //전체 게시글 수
        int totalPages = (int) Math.ceil((double) totalElements / size);//전체 페이지 수

        return new PostListResponse(content, page, size, totalElements, totalPages);
    }

    //게시글 상세 조회
    public PostDetailResponse getPostDetail(Long postId, Long requestUserId) {
        return repo.findDetailById(postId)
                .map(d -> new PostDetailResponse(
                        d.getPostId(),
                        d.getTitle(),
                        d.getAuthorName(),
                        d.getContent(),
                        d.getImages(),
                        d.getLikes(),
                        d.getViews(),
                        d.getCommentsCount(),
                        // 요청자와 글쓴이 id가 같으면 본인 글
                        requestUserId != null && requestUserId.equals(d.getAuthorId()),
                        d.getCreatedAt(),
                        d.getUpdatedAt()
                ))
                .orElse(null);
    }

    //게시글 삭제
    public boolean deletePost(Long postId, Long requesterId) {

        return repo.deleteById(postId);
    }

    // 좋아요 추가
    public Integer addLike(Long postId, Long requesterId) {

        return repo.incrementLikes(postId).orElse(null);
    }

    //좋아요 삭제,취소
    public Integer removeLike(Long postId, Long requesterId) {

        return repo.decrementLikes(postId).orElse(null);
    }

    //댓글 작성
    public CommentResponse createComment(Long postId, Long requesterId, String content) {
        // 테스트위해 작성자 "나"로 하드코딩
        String authorName = "나";

        return repo.addComment(postId, requesterId, authorName, content)
                .map(c -> new CommentResponse(
                        c.getCommentId(),
                        c.getAuthorName(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .orElse(null);
    }

    //댓글 수정
    public UpdateCommentResponse updateComment(Long postId, Long commentId, Long requesterId, String content) {
        return repo.updateComment(postId, commentId, requesterId, content)
                .map(c -> new UpdateCommentResponse(c.getCommentId(), c.getContent()))
                .orElse(null);
    }

    // 댓글 삭제
    public boolean deleteComment(Long postId, Long commentId, Long requesterId) {
        return repo.deleteComment(postId, commentId, requesterId);
    }

    //게시글 수정
    public PostUpdateResponse updatePost(Long postId, String title, String content, String image) {
        return repo.updatePost(postId, title, content, image)
                .map(d -> new PostUpdateResponse(
                        d.getPostId(),
                        d.getTitle(),
                        d.getContent(),
                        // 리스트 → 첫 이미지 (없으면 null)
                        (d.getImages() == null || d.getImages().isEmpty()) ? null : d.getImages().get(0),
                        d.getUpdatedAt()
                ))
                .orElse(null);
    }

    //게시글 생성
    public Post createPost(Long authorId, String authorName, String title, String content, String image) {
        return repo.createPost(authorId, authorName, title, content, image).orElse(null);
    }

}
