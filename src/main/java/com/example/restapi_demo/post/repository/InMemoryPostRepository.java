package com.example.restapi_demo.post.repository;

import com.example.restapi_demo.post.model.Post;
import com.example.restapi_demo.post.model.Comment;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class InMemoryPostRepository implements PostRepository {

    private final List<Post> posts = new ArrayList<>();
    private final Map<Long, List<Comment>> commentsByPost = new HashMap<>();
    private final Map<Long, DetailSeed> detail = new HashMap<>();
    private long commentSeq = 10L;

    public InMemoryPostRepository() {
        // 목록 시드
        posts.add(new Post(1L, "제목 1", "작성자 1", 0, 0, 0));
        posts.add(new Post(2L, "제목 2", "작성자 2", 3, 2, 15));
        // 상세 시드
        detail.put(1L, new DetailSeed(
                1L, 1L, "제목 1", "작성자 1", "본문 내용...",
                List.of("https://example.com/img1.jpg"),
                123, 123, 123,
                LocalDateTime.of(2025,1,1,0,0,0),
                LocalDateTime.of(2025,1,2,10,0,0)
        ));
        detail.put(2L, new DetailSeed(
                2L, 2L, "제목 2", "작성자 2", "두 번째 글 본문...",
                List.of("https://example.com/img2.jpg"),
                7, 42, 3,
                LocalDateTime.of(2025,1,3,9,0,0),
                LocalDateTime.of(2025,1,3,11,30,0)
        ));
    }

    @Override
    public List<Post> findAll() {
        return Collections.unmodifiableList(posts);
    }

    @Override
    public void save(Post post) {
        posts.add(post);
    }

    @Override
    public Optional<DetailSeed> findDetailById(Long postId) {
        return Optional.ofNullable(detail.get(postId));
    }

    @Override
    public boolean deleteById(Long postId) {
        boolean removed = posts.removeIf(p -> p.getPostId().equals(postId));
        detail.remove(postId);
        commentsByPost.remove(postId);
        return removed;
    }

    @Override
    public Optional<Integer> incrementLikes(Long postId) {
        Integer newLikes = null;
        for (Post p : posts) {
            if (p.getPostId().equals(postId)) {
                p.increaseLikes();
                newLikes = p.getLikes();
                break;
            }
        }
        DetailSeed d = detail.get(postId);
        if (d != null) {
            if (newLikes == null) d.setLikes(d.getLikes() + 1);
            else d.setLikes(newLikes);
        }
        return Optional.ofNullable(newLikes != null ? newLikes : (d != null ? d.getLikes() : null));
    }

    @Override
    public Optional<Integer> decrementLikes(Long postId) {
        Integer newLikes = null;
        for (Post p : posts) {
            if (p.getPostId().equals(postId)) {
                p.decreaseLikes();
                newLikes = p.getLikes();
                break;
            }
        }
        DetailSeed d = detail.get(postId);
        if (d != null) {
            if (newLikes == null) d.setLikes(Math.max(0, d.getLikes() - 1));
            else d.setLikes(newLikes);
        }
        return Optional.ofNullable(newLikes != null ? newLikes : (d != null ? d.getLikes() : null));
    }

    private void increaseCommentCount(Long postId) {
        DetailSeed d = detail.get(postId);
        if (d != null) d.setCommentsCount(d.getCommentsCount() + 1);
    }

    @Override
    public Optional<Comment> addComment(Long postId, Long authorId, String authorName, String content) {
        if (!detail.containsKey(postId) && posts.stream().noneMatch(p -> p.getPostId().equals(postId))) {
            return Optional.empty();
        }
        Comment newComment = new Comment(
                ++commentSeq,
                postId, authorId, authorName, content,
                LocalDateTime.now()
        );
        commentsByPost.computeIfAbsent(postId, k -> new ArrayList<>()).add(newComment);
        increaseCommentCount(postId);
        return Optional.of(newComment);
    }

    @Override
    public Optional<Comment> updateComment(Long postId, Long commentId, Long requesterId, String newContent) {
        List<Comment> list = commentsByPost.get(postId);
        if (list == null) return Optional.empty();

        for (Comment c : list) {
            if (c.getCommentId().equals(commentId)) {
                if (!Objects.equals(c.getAuthorId(), requesterId)) return Optional.empty();
                c.updateContent(newContent);
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    private void decreaseCommentCount(Long postId) {
        DetailSeed d = detail.get(postId);
        if (d != null && d.getCommentsCount() > 0) d.setCommentsCount(d.getCommentsCount() - 1);
    }

    @Override
    public boolean deleteComment(Long postId, Long commentId, Long requesterId) {
        List<Comment> list = commentsByPost.get(postId);
        if (list == null) return false;

        boolean removed = list.removeIf(c -> c.getCommentId().equals(commentId)
                && Objects.equals(c.getAuthorId(), requesterId));

        if (removed) {
            decreaseCommentCount(postId);
            if (list.isEmpty()) commentsByPost.remove(postId);
        }
        return removed;
    }

    @Override
    public Optional<DetailSeed> updatePost(Long postId, String newTitle, String newContent, String newImage) {
        DetailSeed d = detail.get(postId);
        if (d == null) return Optional.empty();

        for (Post p : posts) {
            if (p.getPostId().equals(postId)) {
                if (newTitle != null) p.setTitle(newTitle);
                break;
            }
        }
        if (newTitle != null)   d.setTitle(newTitle);
        if (newContent != null) d.setContent(newContent);
        if (newImage != null)   d.setImages(List.of(newImage));
        d.setUpdatedAt(LocalDateTime.now());

        return Optional.of(d);
    }

    @Override
    public Optional<Post> createPost(Long authorId, String authorName, String title, String content, String image) {
        if (title == null || content == null) return Optional.empty();

        long newId = posts.size() + 1L;
        Post newPost = new Post(newId, title, authorName, 0, 0, 0);
        posts.add(newPost);

        detail.put(newId, new DetailSeed(
                newId, authorId, title, authorName, content,
                List.of(image != null ? image : ""),
                0, 0, 0,
                LocalDateTime.now(), LocalDateTime.now()
        ));

        return Optional.of(newPost);
    }
}
