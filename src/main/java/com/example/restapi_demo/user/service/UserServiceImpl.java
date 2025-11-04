package com.example.restapi_demo.user.service;

import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    @Override
    public User register(String email, String password, String passwordConfirm,
                         String nickname, String profileImageUrl) {
        if (email == null || email.isBlank()
                || password == null || passwordConfirm == null
                || !password.equals(passwordConfirm)) {
            return null;
        }

        User toSave = User.builder()
                .email(email)
                .passwordHash(password)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();

        return userRepository.save(toSave);
    }

    @Override
    public User findById(Long id) {
        if (id == null) return null;
        return userRepository.findById(id).orElse(null);
    }


    @Override
    public User updateProfile(Long id, String nickname, String profileImageUrl) {
        if (id == null) return null;
        return userRepository.updateProfile(id, nickname, profileImageUrl).orElse(null);
    }


    @Override
    public boolean deleteMe(Long id) {
        if (id == null) return false;
        return userRepository.deleteById(id);
    }


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

        Optional<User> updated = userRepository.updatePassword(id, newPassword); // 내부에서 passwordHash로 저장
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
                .filter(u -> password.equals(u.getPasswordHash()))
                .orElse(null);
    }


    @Override
    @Transactional(readOnly = true)
    public List<User> findByNicknameKeyword(String keyword) {

        return userRepository.findByNicknameContainingIgnoreCaseOrderByIdDesc(keyword);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    @Transactional(readOnly = true)
    public long countByNickname(String nickname) {
        return userRepository.countByNickname(nickname);
    }
}
