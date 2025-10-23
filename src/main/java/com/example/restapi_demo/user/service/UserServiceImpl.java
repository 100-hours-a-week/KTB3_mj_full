package com.example.restapi_demo.user.service;

import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.repository.UserRepository; // 인터페이스
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // 추상화에 의존

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    @Override
    public User register(String email, String password, String passwordConfirm,
                         String nickname, String profileImage) {
        if (email == null || email.isBlank()
                || password == null || passwordConfirm == null
                || !password.equals(passwordConfirm)) {
            return null;
        }

        User toSave = new User(null, email, password, nickname, profileImage);
        return userRepository.save(toSave);
    }


    @Override
    public User findById(Long id) {
        if (id == null) return null;
        return userRepository.findById(id).orElse(null);
    }

    // 프로필 수정
    @Override
    public User updateProfile(Long id, String nickname, String profileImage) {
        if (id == null) return null;
        // repo가 내부적으로 존재 확인/수정 후 Optional 반환
        return userRepository.updateProfile(id, nickname, profileImage).orElse(null);
    }

    // 회원 삭제
    @Override
    public boolean deleteMe(Long id) {
        if (id == null) return false;
        return userRepository.deleteById(id);
    }

    // 비밀번호 변경
    @Override
    public ChangePasswordResult changePassword(Long id, String newPassword, String newPasswordConfirm) {
        List<String[]> errors = new ArrayList<>();
        if (id == null) {
            errors.add(new String[]{"id", "invalid"});
            return new ChangePasswordResult(false, errors);
        }
        if (newPassword == null || newPasswordConfirm == null || !newPassword.equals(newPasswordConfirm)) {
            errors.add(new String[]{"password", "mismatch"});
            return new ChangePasswordResult(false, errors);
        }


        Optional<User> updated = userRepository.updatePassword(id, newPassword);
        if (updated.isEmpty()) {
            errors.add(new String[]{"user", "not_found"});
            return new ChangePasswordResult(false, errors);
        }
        return new ChangePasswordResult(true, null);
    }


    @Override
    public User authenticate(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) return null;


        return userRepository.findByEmail(email)
                .filter(u -> password.equals(u.getPassword()))
                .orElse(null);
    }
}
