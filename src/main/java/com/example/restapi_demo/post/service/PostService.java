package com.example.restapi_demo.post.service;

import com.example.restapi_demo.post.dto.*;
import com.example.restapi_demo.post.model.Post;

public interface PostService {
    PostListResponse getPosts();
    PostDetailResponse getPostDetail(Long postId, Long requestUserId);
    boolean deletePost(Long postId, Long requesterId);
    Integer addLike(Long postId, Long requesterId);
    Integer removeLike(Long postId, Long requesterId);
    CommentResponse createComment(Long postId, Long requesterId, String content);
    UpdateCommentResponse updateComment(Long postId, Long commentId, Long requesterId, String content);
    boolean deleteComment(Long postId, Long commentId, Long requesterId);
    PostUpdateResponse updatePost(Long postId, String title, String content, String image);
    Post createPost(Long authorId, String authorName, String title, String content, String image);
}
