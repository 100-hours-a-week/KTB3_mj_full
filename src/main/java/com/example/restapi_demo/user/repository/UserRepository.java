package com.example.restapi_demo.user.repository;

import com.example.restapi_demo.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User u);
    Optional<User> findById(Long id);
    Optional<User> updateProfile(Long id, String nickname, String profileImage);
    boolean deleteById(Long id);
    Optional<User> updatePassword(Long id, String newPassword);

    List<User> findByNicknameContainingIgnoreCaseOrderByIdDesc(String keyword);
    boolean existsByEmail(String email);
    long countByNickname(String nickname);
}
