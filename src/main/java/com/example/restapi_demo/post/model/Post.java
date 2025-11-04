package com.example.restapi_demo.post.model;

import com.example.restapi_demo.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "author_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_posts_author")
    )
    private User author;

    @Column(name = "title", nullable = false, length = 26)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "longtext")
    private String content;


    @Builder.Default
    @Column(name = "views", nullable = false)
    private Integer views = 0;

    @Builder.Default
    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;

    @Builder.Default
    @Column(name = "comments_count", nullable = false)
    private Integer commentsCount = 0;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC, id ASC")
    @Builder.Default
    private List<PostImage> images = new ArrayList<>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC, id ASC")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();


    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
        if (views == null) views = 0;
        if (likesCount == null) likesCount = 0;
        if (commentsCount == null) commentsCount = 0;
        if (isDeleted == null) isDeleted = false;
    }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }


    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }


    public void addImage(PostImage image) {
        images.add(image);
        image.setPost(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
        this.commentsCount++;
    }

    public void increaseLikes() { this.likesCount++; }

    public void decreaseLikes() {
        this.likesCount = Math.max(0, this.likesCount - 1);
    }
}
