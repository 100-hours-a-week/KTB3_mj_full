package com.example.restapi_demo.user.controller;

import com.example.restapi_demo.common.api.ApiResponse;
import com.example.restapi_demo.user.dto.FieldErrorDTO;
import com.example.restapi_demo.user.dto.PasswordChangeRequest;
import com.example.restapi_demo.user.dto.UpdateUserRequest;
import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.service.UserService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService = new UserService();

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> signup(@RequestBody Map<String, String> req) {
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

    /** ✅ 내 정보 조회: GET /users/me */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> me(
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        try {//인증 체크
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long id = null;
            try { id = Long.parseLong(userId); } catch (Exception ignore) {}


            //대상조회
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

    /** ✅ 내 정보 수정: PATCH /users/me */
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Object>> updateMe(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestBody UpdateUserRequest req
    ) {
        try {//인증체크
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }
            Long id = null;
            try { id = Long.parseLong(userId); } catch (Exception ignore) {}

            // 유효성 검사
            List<FieldErrorDTO> errors = new ArrayList<>();
            String nickname = req.getNickname();
            String profileImage = req.getProfile_image();

            if (nickname == null || nickname.isBlank()) {
                errors.add(new FieldErrorDTO("nickname", "blank"));
            } else if (nickname.length() > 20) {//길이제한 있음
                errors.add(new FieldErrorDTO("nickname", "too_long"));
            }
            if (!errors.isEmpty()) {//닉넴 필수
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", errors));
            }

            //대상조회
            User exist = userService.findById(id);
            if (exist == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            //프로필 수정
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

    /** ✅ 회원탈퇴: DELETE /users/me */
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Object>> deleteMe(
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        try {
            // 인증체크
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

    /** ✅ 비밀번호 변경: PATCH /users/me/password */
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestBody PasswordChangeRequest req
    ) {
        try {
            // 인증체크
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }

            Long id = null;
            try { id = Long.parseLong(userId); } catch (Exception ignore) {}

            // 서비스 호출
            var result = userService.changePassword(id, req.getNew_password(), req.getNew_password_confirm());

            // 대상 없음 (존재하지 않는 유저)
            if (result == null || (result.errors == null && !result.success)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("internal_server_error", null));
            }

            // 유효성 실패
            if (!result.success) {
                List<FieldErrorDTO> errors = new ArrayList<>();
                for (String[] e : result.errors) {
                    errors.add(new FieldErrorDTO(e[0], e[1]));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", errors));
            }

            // 성공
            return ResponseEntity.ok(new ApiResponse<>("password_changed", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }
}
