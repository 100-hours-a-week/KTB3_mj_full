package com.example.restapi_demo.post.controller;

import com.example.restapi_demo.common.api.ApiResponse;
import com.example.restapi_demo.post.dto.*;
import com.example.restapi_demo.post.model.Post;
import com.example.restapi_demo.post.service.PostService;
import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.service.UserService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.example.restapi_demo.post.dto.PostUpdateRequest;
import com.example.restapi_demo.post.dto.PostUpdateResponse;
import com.example.restapi_demo.post.dto.FieldErrorDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService = new PostService();

    private final UserService userService = new UserService();

    // 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> list(
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            PostListResponse data = postService.getPosts();
            return ResponseEntity.ok(new ApiResponse<>("read_success", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> detail(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long postId
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }

            Long requester = null;
            try { requester = Long.parseLong(userId); } catch (Exception ignore) {}

            PostDetailResponse data = postService.getPostDetail(postId, requester);
            if (data == null) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }
            return ResponseEntity.ok(new ApiResponse<>("read_success", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

    //게시글 삭제 성공시 204
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> delete(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long postId
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }

            Long requester = null;
            try { requester = Long.parseLong(userId); } catch (Exception ignore) {}

            boolean deleted = postService.deletePost(postId, requester);
            if (!deleted) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("delete_success", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }



    // 좋아요 추가

    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Object>> like(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long postId
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long requester = null;
            try { requester = Long.parseLong(userId); } catch (Exception ignore) {}

            Integer newLikes = postService.addLike(postId, requester);
            if (newLikes == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            return ResponseEntity.ok(
                    new ApiResponse<>("like_added", Map.of("likes", newLikes))
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

    //좋아요 취소
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Object>> unlike(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long postId
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long requester = null;
            try { requester = Long.parseLong(userId); } catch (Exception ignore) {}

            Integer newLikes = postService.removeLike(postId, requester);
            if (newLikes == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            return ResponseEntity.ok(
                    new ApiResponse<>("like_removed", Map.of("likes", newLikes))
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }



    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<Object>> createComment(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }

            Long requester = null;
            try { requester = Long.parseLong(userId); } catch (Exception ignore) {}

            CommentResponse data = postService.createComment(postId, requester, request.getContent());
            if (data == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("create_success", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

    // 댓글 수정
    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> updateComment(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long requester = null;
            try { requester = Long.parseLong(userId);
            } catch (Exception ignore) {}

            UpdateCommentResponse data =
                    postService.updateComment(postId, commentId, requester, request.getContent());

            if (data == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            return ResponseEntity.ok(new ApiResponse<>("update_success", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

    //댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long requester = null;
            try { requester = Long.parseLong(userId); } catch (Exception ignore) {}

            boolean ok = postService.deleteComment(postId, commentId, requester);
            if (!ok) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }


            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("delete_success", null));



        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> updatePost(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }


            List<FieldErrorDTO> errors = new ArrayList<>();

            String title = request.getTitle();
            String content = request.getContent();
            String image = request.getImage();

            if (title == null || title.isBlank()) {
                errors.add(new FieldErrorDTO("title", "blank"));
            } else if (title.length() > 26) {
                errors.add(new FieldErrorDTO("title", "too_long"));
            }

            if (content == null || content.isBlank()) {
                errors.add(new FieldErrorDTO("content", "blank"));
            }

            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", errors));
            }


            PostUpdateResponse data = postService.updatePost(postId, title, content, image);
            if (data == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("post_not_found", null));
            }

            return ResponseEntity.ok(new ApiResponse<>("update_success", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createPost(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestBody Map<String, String> req
    ) {
        try {

            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }

            String title = req.get("title");
            String content = req.get("content");
            String image = req.get("image");

            // 간단 유효성(title.length()>26)
            if (title == null || title.isBlank() || content == null || content.isBlank() || title.length() > 26) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", null));
            }

            // 작성자 존재 확인
            Long authorId = Long.parseLong(userId);
            User author = userService.findById(authorId);
            if (author == null) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            // 작성자 이름(닉네임) 사용
            String authorName = author.getNickname() != null ? author.getNickname() : "나";

            Post newPost = postService.createPost(authorId, authorName, title, content, image);
            if (newPost == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            Map<String, Object> data = Map.of(
                    "post_id", newPost.getPostId(),
                    "title", newPost.getTitle(),
                    "author", authorName,
                    "image", image,
                    "created_at", newPost.getCreatedAt()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("create_success", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

}
