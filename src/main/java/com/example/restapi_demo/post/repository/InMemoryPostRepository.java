package com.example.restapi_demo.post.repository;

import com.example.restapi_demo.post.model.Post;
import com.example.restapi_demo.post.model.Comment;

import java.time.LocalDateTime;
import java.util.*;

//InMemoryPostRepository
public class InMemoryPostRepository {

    private final List<Post> posts = new ArrayList<>();//게시글 목록

    private final Map<Long, List<Comment>> commentsByPost = new HashMap<>();//게시글별 댓글 저장소
    private long commentSeq = 10L;


    // 데이터값 입력하기 위해
    public static class DetailSeed {
        private Long postId;
        private Long authorId;
        private String title;
        private String authorName;
        private String content;
        private List<String> images;
        private int likes;
        private int views;
        private int commentsCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DetailSeed(Long postId, Long authorId, String title, String authorName, String content,
                          List<String> images, int likes, int views, int commentsCount,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.postId = postId;
            this.authorId = authorId;
            this.title = title;
            this.authorName = authorName;
            this.content = content;
            this.images = images;
            this.likes = likes;
            this.views = views;
            this.commentsCount = commentsCount;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }


        void setLikes(int likes) { this.likes = likes; }
        void incrementLikes() { this.likes++; }
        void decrementLikes() { if (this.likes > 0) this.likes--; }


        void setTitle(String title) { this.title = title; }
        void setContent(String content) { this.content = content; }
        void setImages(List<String> images) { this.images = images; }
        void setUpdatedAt(LocalDateTime t) { this.updatedAt = t; }


        public Long getPostId() { return postId; }
        public Long getAuthorId() { return authorId; }
        public String getTitle() { return title; }
        public String getAuthorName() { return authorName; }
        public String getContent() { return content; }
        public List<String> getImages() { return images; }
        public int getLikes() { return likes; }
        public int getViews() { return views; }
        public int getCommentsCount() { return commentsCount; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
    }

    private final Map<Long, DetailSeed> detail = new HashMap<>();//게시글 상세 저장소

    public InMemoryPostRepository() {
        // 목록 시드
        posts.add(new Post(1L, "제목 1", "작성자 1", 0, 0, 0));
        posts.add(new Post(2L, "제목 2", "작성자 2", 3, 2, 15));

        // 상세 시드
        detail.put(1L, new DetailSeed(//api설계서에 따라 우선 하드코딩
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

    //목록 조회
    public List<Post> findAll() {
        return Collections.unmodifiableList(posts);
    }

    //목록 시드 저장
    public void save(Post post) {
        posts.add(post);
    }

    //상세 조회
    public Optional<DetailSeed> findDetailById(Long postId) {
        return Optional.ofNullable(detail.get(postId));
    }

    //게시글 삭제
    public boolean deleteById(Long postId) {
        boolean removed = posts.removeIf(p -> p.getPostId().equals(postId));
        detail.remove(postId);
        return removed;
    }

    //좋아요 누르면 증가
    public Optional<Integer> incrementLikes(Long postId) {
        Integer newLikes = null;

        // 1) 목록 시드(Post)에서 증가
        for (Post p : posts) {
            if (p.getPostId().equals(postId)) {
                p.increaseLikes();
                newLikes = p.getLikes();
                break;
            }
        }

        // 2) 상세 시드 동기화
        DetailSeed d = detail.get(postId);
        if (d != null) {
            if (newLikes == null) {        // 목록에 없으면 상세 기준으로 증가
                d.incrementLikes();
                newLikes = d.getLikes();
            } else {
                d.setLikes(newLikes);       // 목록과 숫자 맞추기
            }
        }

        return Optional.ofNullable(newLikes);
    }

    // 게시글 좋아요 취소
    public Optional<Integer> decrementLikes(Long postId) {
        Integer newLikes = null;

        // 1) 목록 시드(Post)에서 감소
        for (Post p : posts) {
            if (p.getPostId().equals(postId)) {
                p.decreaseLikes();
                newLikes = p.getLikes();
                break;
            }
        }

        // 2) 상세 시드 동기화
        DetailSeed d = detail.get(postId);
        if (d != null) {
            if (newLikes == null) {        // 목록에 없으면 상세 기준으로 감소
                d.decrementLikes();
                newLikes = d.getLikes();
            } else {
                d.setLikes(newLikes);       // 목록과 숫자 맞추기
            }
        }

        return Optional.ofNullable(newLikes);
    }

    private void increaseCommentCount(Long postId) {
        DetailSeed d = detail.get(postId);
        if (d != null) {
            d.commentsCount = d.getCommentsCount() + 1;
        }
    }

    //댓글 추가
    public Optional<Comment> addComment(Long postId, Long authorId, String authorName, String content) {
        // 존재하는 게시글인지 최소 확인 (상세 시드 또는 목록에 존재)
        if (!detail.containsKey(postId) && posts.stream().noneMatch(p -> p.getPostId().equals(postId))) {
            return Optional.empty();
        }

        Comment newComment = new Comment(
                ++commentSeq,
                postId,
                authorId,
                authorName,
                content,
                LocalDateTime.now()
        );

        commentsByPost.computeIfAbsent(postId, k -> new ArrayList<>()).add(newComment);
        increaseCommentCount(postId); // 상세 시드의 commentsCount 동기화

        return Optional.of(newComment);
    }

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

    // 댓글 수 감소
    private void decreaseCommentCount(Long postId) {
        DetailSeed d = detail.get(postId);
        if (d != null && d.getCommentsCount() > 0) {
            d.commentsCount = d.getCommentsCount() - 1;
        }
    }

    //댓글 삭제
    public boolean deleteComment(Long postId, Long commentId, Long requesterId) {
        List<Comment> list = commentsByPost.get(postId);
        if (list == null) return false;

        // 작성자만 삭제 가능하려면 authorId 비교 로직 추가 가능
        boolean removed = list.removeIf(c -> c.getCommentId().equals(commentId)
                && Objects.equals(c.getAuthorId(), requesterId) );

        if (removed) {
            decreaseCommentCount(postId);
            // 리스트가 비면 맵에서 치워도 됨
            if (list.isEmpty()) commentsByPost.remove(postId);
        }
        return removed;
    }

    //게시글 수정
    public Optional<DetailSeed> updatePost(Long postId, String newTitle, String newContent, String newImage) {
        // 상세 시드 존재 확인
        DetailSeed d = detail.get(postId);
        if (d == null) return Optional.empty();

        // 1) 목록 시드의 제목도 맞춰줌
        for (Post p : posts) {
            if (p.getPostId().equals(postId)) {
                if (newTitle != null) p.setTitle(newTitle);
                break;
            }
        }

        // 2) 상세 시드 업데이트
        if (newTitle != null)   d.setTitle(newTitle);
        if (newContent != null) d.setContent(newContent);
        if (newImage != null)   d.setImages(List.of(newImage)); // 단일 이미지 → 리스트로 저장
        d.setUpdatedAt(LocalDateTime.now());

        return Optional.of(d);
    }

    public Optional<Post> createPost(Long authorId, String authorName, String title, String content, String image) {
        if (title == null || content == null) return Optional.empty();

        long newId = posts.size() + 1L;
        Post newPost = new Post(newId, title, authorName, 0, 0, 0);
        posts.add(newPost);

        // 상세 시드도 생성
        detail.put(newId, new DetailSeed(
                newId, authorId, title, authorName, content,
                List.of(image != null ? image : ""),
                0, 0, 0,
                LocalDateTime.now(), LocalDateTime.now()
        ));

        return Optional.of(newPost);
    }

}
