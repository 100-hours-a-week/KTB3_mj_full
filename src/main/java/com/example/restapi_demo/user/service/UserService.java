package com.example.restapi_demo.user.service;

import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.repository.InMemoryUserRepository;

import java.util.ArrayList;
import java.util.List;

//UserService 사용자 관련 비즈니스 로직 -> 유효성검사, 저장/조회

public class UserService {
    // static 사용해야됨 전역으로 공유해야
    private final InMemoryUserRepository repo = new InMemoryUserRepository();

    public static class ValidationResult {
        public final boolean success;
        public final List<String[]> errors;

        public ValidationResult(boolean success, List<String[]> errors) {
            this.success = success;
            this.errors = errors;
        }
    }

    //회원가입 이메일 비번 닉네임 체크
    public User register(String email, String password, String passwordConfirm,
                         String nickname, String profileImage) {

        if (email == null || !email.contains("@") || !email.contains(".")) return null;
        if (password == null || passwordConfirm == null || !password.equals(passwordConfirm)) return null;
        if (nickname == null || nickname.isBlank()) return null;
        if (repo.findByEmail(email).isPresent()) return null;

        return repo.save(new User(null, email, password, nickname, profileImage));
    }

    //로그인 이메일로 사용자 확인,비밀번호 확인
    public User authenticate(String email, String password) {
        if (email == null || password == null) return null;
        return repo.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    //ID로 사용자 조회
    public User findById(Long id) {
        if (id == null) return null;
        return repo.findById(id).orElse(null);
    }


    //프로필 수정
    public User updateProfile(Long id, String nickname, String profileImage) {
        return repo.updateProfile(id, nickname, profileImage).orElse(null);
    }

    // 회원탈퇴
    public boolean deleteMe(Long id) {
        if (id == null) return false;
        return repo.deleteById(id);
    }

    // 비밀번호 조건 8~20자 대문자,소문자, 숫자, 특수문자 각 1개 이상 포함
    //나중에 해시적용하라고 제안
    private boolean isValidPassword(String pw) {
        if (pw == null) return false;
        return pw.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,20}$");
    }

    // 비밀번호 변경
    public ValidationResult changePassword(Long userId, String newPassword, String newPasswordConfirm) {
        List<String[]> errs = new ArrayList<>();

        //비밀번호 형식검사
        if (!isValidPassword(newPassword)) {
            errs.add(new String[]{"new_password", "invalid_format"});
        }
        //비밀번호 일치검사
        if (newPassword == null || newPasswordConfirm == null || !newPassword.equals(newPasswordConfirm)) {
            errs.add(new String[]{"new_password_confirm", "not_match"});
        }
        //비밀번호 공백검사
        if (!errs.isEmpty()) return new ValidationResult(false, errs);

        // 대상 사용자 확인
        User u = repo.findById(userId).orElse(null);
        if (u == null) return new ValidationResult(false, null); // 컨트롤러에서 500 처리

        // 저장
        repo.updatePassword(userId, newPassword);
        return new ValidationResult(true, errs);
    }



}
