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
                        p.getId(),                               // ✅ getPostId() → getId()
                        p.getTitle(),
                        p.getAuthor() != null ?                  // ✅ 작성자 표시는 닉네임로
                                p.getAuthor().getNickname() : null,
                        p.getLikesCount(),                       // ✅ getLikes() → getLikesCount()
                        p.getCommentsCount(),                    // ✅ getComments() → getCommentsCount()
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
                        d.getLikesCount(),                       // ✅ getLikes() → getLikesCount()
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
        // authorName은 레거시 호환용으로 repo가 처리한다면 유지,
        // 엔티티 기준이면 repo에서 User로 resolve 하도록 구현되어야 합니다.
        String authorName = "나";
        return repo.addComment(postId, requesterId, authorName, content)
                .map(c -> new CommentResponse(
                        c.getId(),                               // ✅ commentId() → id
                        (c.getAuthor() != null
                                ? c.getAuthor().getNickname()
                                : authorName),                   // ✅ authorName → author.nickname
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .orElse(null);
    }

    @Override
    public UpdateCommentResponse updateComment(Long postId, Long commentId, Long requesterId, String content) {
        return repo.updateComment(postId, commentId, requesterId, content)
                .map(c -> new UpdateCommentResponse(c.getId(), c.getContent()))
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

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PostSummary> searchByTitle(String keyword) {
        return repo.findByTitleContainingIgnoreCase(keyword).stream()
                .map(p -> new PostSummary(
                        p.getId(),
                        p.getTitle(),
                        p.getAuthor() != null ? p.getAuthor().getNickname() : null,
                        p.getLikesCount(),
                        p.getCommentsCount(),
                        p.getViews(),
                        p.getCreatedAt()
                ))
                .toList();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PostSummary> findByAuthorNickname(String nickname) {
        return repo.findByAuthorNickname(nickname).stream()
                .map(p -> new PostSummary(
                        p.getId(),
                        p.getTitle(),
                        p.getAuthor() != null ? p.getAuthor().getNickname() : null,
                        p.getLikesCount(),
                        p.getCommentsCount(),
                        p.getViews(),
                        p.getCreatedAt()
                ))
                .toList();
    }
}
