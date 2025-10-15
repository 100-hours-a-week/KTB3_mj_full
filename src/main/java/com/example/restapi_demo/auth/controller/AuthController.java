package com.example.restapi_demo.auth.controller;

import com.example.restapi_demo.common.api.ApiResponse;
import com.example.restapi_demo.auth.dto.LoginRequest;
import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.service.UserService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService = new UserService();

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody LoginRequest req) {
        try {

            if (req == null || req.getEmail() == null || req.getEmail().isBlank()
                    || req.getPassword() == null || req.getPassword().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", null));
            }

            //이메일 비밀번호 검증
            User user = userService.authenticate(req.getEmail(), req.getPassword());
            if (user == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("invalid_request", null));
            }

            // 로그인 성공
            return ResponseEntity.ok(
                    new ApiResponse<>("loginSuccess", Map.of("user_id", user.getId()))
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        try {
            //인증 헤더
            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("auth_required", null));
            }


            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("logout_success", null));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("internal_server_error", null));
        }
    }
}
