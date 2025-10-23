package com.example.restapi_demo.post.service;

import com.example.restapi_demo.post.dto.*;
import com.example.restapi_demo.post.model.Post;
import com.example.restapi_demo.post.repository.PostRepository;
import com.example.restapi_demo.post.repository.PostRepository.DetailSeed;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repo;

    public PostServiceImpl(PostRepository repo) {
        this.repo = repo;
    }

    @Override
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

        int page = 0;
        int size = 10;
        long totalElements = content.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PostListResponse(content, page, size, totalElements, totalPages);
    }

    @Override
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
                        requestUserId != null && requestUserId.equals(d.getAuthorId()),
                        d.getCreatedAt(),
                        d.getUpdatedAt()
                ))
                .orElse(null);
    }

    @Override
    public boolean deletePost(Long postId, Long requesterId) {
        return repo.deleteById(postId);
    }

    @Override
    public Integer addLike(Long postId, Long requesterId) {
        return repo.incrementLikes(postId).orElse(null);
    }

    @Override
    public Integer removeLike(Long postId, Long requesterId) {
        return repo.decrementLikes(postId).orElse(null);
    }

    @Override
    public CommentResponse createComment(Long postId, Long requesterId, String content) {
        String authorName = "나"; // 테스트용 하드코딩 유지
        return repo.addComment(postId, requesterId, authorName, content)
                .map(c -> new CommentResponse(
                        c.getCommentId(),
                        c.getAuthorName(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .orElse(null);
    }

    @Override
    public UpdateCommentResponse updateComment(Long postId, Long commentId, Long requesterId, String content) {
        return repo.updateComment(postId, commentId, requesterId, content)
                .map(c -> new UpdateCommentResponse(c.getCommentId(), c.getContent()))
                .orElse(null);
    }

    @Override
    public boolean deleteComment(Long postId, Long commentId, Long requesterId) {
        return repo.deleteComment(postId, commentId, requesterId);
    }

    @Override
    public PostUpdateResponse updatePost(Long postId, String title, String content, String image) {
        return repo.updatePost(postId, title, content, image)
                .map((DetailSeed d) -> new PostUpdateResponse(
                        d.getPostId(),
                        d.getTitle(),
                        d.getContent(),
                        (d.getImages() == null || d.getImages().isEmpty()) ? null : d.getImages().get(0),
                        d.getUpdatedAt()
                ))
                .orElse(null);
    }

    @Override
    public Post createPost(Long authorId, String authorName, String title, String content, String image) {
        return repo.createPost(authorId, authorName, title, content, image).orElse(null);
    }
}
