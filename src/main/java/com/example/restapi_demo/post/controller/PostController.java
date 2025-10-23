package com.example.restapi_demo.post.controller;

import com.example.restapi_demo.common.api.ApiResponse;
import com.example.restapi_demo.post.dto.*;
import com.example.restapi_demo.post.model.Post;
import com.example.restapi_demo.post.service.PostService;
import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {


    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }


    @Operation(
            summary = "게시글 목록 조회",
            description = "전체 게시글 목록을 조회합니다. X-User-Id 헤더가 필요합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostListResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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


    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 기반으로 상세 내용을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDetailResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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


    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. 성공 시 204 반환.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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


    @Operation(summary = "좋아요 추가", description = "게시글에 좋아요를 추가합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LikeCountDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
            return ResponseEntity.ok(new ApiResponse<>("like_added", Map.of("likes", newLikes)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }


    @Operation(summary = "좋아요 취소", description = "게시글의 좋아요를 취소합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LikeCountDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
            return ResponseEntity.ok(new ApiResponse<>("like_removed", Map.of("likes", newLikes)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }


    @Operation(summary = "댓글 작성", description = "게시글에 새로운 댓글을 작성합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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


    @Operation(summary = "댓글 수정", description = "특정 댓글의 내용을 수정합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCommentResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
            try { requester = Long.parseLong(userId); } catch (Exception ignore) {}
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


    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다. 성공 시 204 반환.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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


    @Operation(summary = "게시글 수정", description = "제목/본문/이미지를 수정합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostUpdateResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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


    @Operation(summary = "게시글 생성", description = "새로운 게시글을 작성합니다. 제목은 최대 26자 제한이 있습니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
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

            if (title == null || title.isBlank() || content == null || content.isBlank() || title.length() > 26) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", null));
            }

            Long authorId = Long.parseLong(userId);
            User author = userService.findById(authorId);
            if (author == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            String authorName = author.getNickname() != null ? author.getNickname() : "나";
            Post newPost = postService.createPost(authorId, authorName, title, content, image);
            if (newPost == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            PostCreateResponse data = new PostCreateResponse(
                    newPost.getPostId(),
                    newPost.getTitle(),
                    authorName,
                    image,
                    newPost.getCreatedAt()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("create_success", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }
}
