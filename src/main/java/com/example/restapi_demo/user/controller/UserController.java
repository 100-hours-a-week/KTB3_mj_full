package com.example.restapi_demo.user.controller;

import com.example.restapi_demo.common.api.ApiResponse;
import com.example.restapi_demo.user.dto.FieldErrorDTO;
import com.example.restapi_demo.user.dto.PasswordChangeRequest;
import com.example.restapi_demo.user.dto.UpdateUserRequest;
import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(
            summary = "회원가입",
            description = "이메일/비밀번호/닉네임으로 회원을 등록합니다. 성공 시 data에 user_id 반환."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "register_success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "invalid_request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> signup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "회원가입 요청 바디",
                    content = @Content(schema = @Schema(implementation = SignupBody.class))
            )
            @RequestBody Map<String, String> req) {
        try {
            String email = req.get("email");
            String password = req.get("password");
            String passwordConfirm = req.get("password_confirm");
            String nickname = req.get("nickname");
            String profileImage = req.get("profile_image");

            User user = userService.register(email, password, passwordConfirm, nickname, profileImage);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", null));
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("register_success", Map.of("user_id", user.getId())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }


    @Operation(summary = "내 정보 조회", description = "헤더 X-User-Id 기준으로 로그인 사용자의 정보를 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "read_success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "auth_required"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> me(
            @Parameter(description = "로그인 사용자 ID", example = "1")
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long id = null;
            try { id = Long.parseLong(userId); } catch (Exception ignore) {}

            User u = userService.findById(id);
            if (u == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            Map<String, Object> data = Map.of(
                    "id", u.getId(),
                    "email", u.getEmail(),
                    "nickname", u.getNickname(),
                    "profile_image", u.getProfileImage()
            );
            return ResponseEntity.ok(new ApiResponse<>("read_success", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }


    @Operation(summary = "내 정보 수정", description = "닉네임(필수, 최대 20자)과 프로필 이미지(선택)를 수정합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "update_success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "invalid_request",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldErrorDTO.class)))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "auth_required"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Object>> updateMe(
            @Parameter(description = "로그인 사용자 ID", example = "1")
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateUserRequest.class))
            )
            @RequestBody UpdateUserRequest req
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long id = null;
            try { id = Long.parseLong(userId); } catch (Exception ignore) {}

            List<FieldErrorDTO> errors = new ArrayList<>();
            String nickname = req.getNickname();
            String profileImage = req.getProfile_image();

            if (nickname == null || nickname.isBlank()) {
                errors.add(new FieldErrorDTO("nickname", "blank"));
            } else if (nickname.length() > 20) {
                errors.add(new FieldErrorDTO("nickname", "too_long"));
            }
            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", errors));
            }

            User exist = userService.findById(id);
            if (exist == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            User updated = userService.updateProfile(id, nickname, profileImage);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", updated.getId());
            data.put("nickname", updated.getNickname());
            data.put("profile_image", updated.getProfileImage());
            data.put("updated_at", LocalDateTime.now());

            return ResponseEntity.ok(new ApiResponse<>("update_success", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }


    @Operation(summary = "회원 탈퇴", description = "성공 시 204(No Content) 반환.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "delete_success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "auth_required"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Object>> deleteMe(
            @Parameter(description = "로그인 사용자 ID", example = "1")
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long id = null;
            try { id = Long.parseLong(userId); } catch (Exception ignore) {}

            boolean deleted = userService.deleteMe(id);
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


    @Operation(summary = "비밀번호 변경",
            description = "8~20자, 대/소문자/숫자/특수문자 각 1개 이상 포함하는 새 비밀번호로 변경합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "password_changed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "invalid_request",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldErrorDTO.class)))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "auth_required"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @Parameter(description = "로그인 사용자 ID", example = "1")
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = PasswordChangeRequest.class))
            )
            @RequestBody PasswordChangeRequest req
    ) {
        try {
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }

            Long id = null;
            try { id = Long.parseLong(userId); } catch (Exception ignore) {}

            var result = userService.changePassword(id, req.getNew_password(), req.getNew_password_confirm());

            if (result == null || (result.errors == null && !result.success)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            if (!result.success) {
                List<FieldErrorDTO> errors = new ArrayList<>();
                for (String[] e : result.errors) errors.add(new FieldErrorDTO(e[0], e[1]));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", errors));
            }

            return ResponseEntity.ok(new ApiResponse<>("password_changed", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }


    @Schema(description = "회원가입 요청 바디(문서용)")
    static class SignupBody {
        @Schema(description = "이메일", example = "test@startupcode.kr")
        public String email;
        @Schema(description = "비밀번호", example = "test1234")
        public String password;
        @Schema(description = "비밀번호 확인", example = "test1234")
        public String password_confirm;
        @Schema(description = "닉네임", example = "startup")
        public String nickname;
        @Schema(description = "프로필 이미지 URL", example = "https://image.kr/img.jpg")
        public String profile_image;
    }
}
