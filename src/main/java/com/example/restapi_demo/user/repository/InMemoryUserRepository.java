package com.example.restapi_demo.user.repository;

import com.example.restapi_demo.user.model.User;
import java.util.*;

public class InMemoryUserRepository {
    // 전역으로 선언해야 함
    private static final Map<Long, User> USERS = new HashMap<>();
    //사용자 들어올때마다 증가
    private static long NEXT_ID = 1L;



    // 이메일로 조회 NPE방지하는 코드
    public Optional<User> findByEmail(String email) {
        return USERS.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    //회원 저장 Map에 등록한다
    public User save(User u) {
        User saved = new User(NEXT_ID++, u.getEmail(), u.getPassword(), u.getNickname(), u.getProfileImage());
        USERS.put(saved.getId(), saved);
        return saved;
    }

    //ID러 조회
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(USERS.get(id));//없으ㅡ면 Optional.empty
    }

    // 프로필 업데이트
    public Optional<User> updateProfile(Long id, String nickname, String profileImage) {
        User u = USERS.get(id);
        if (u == null) return Optional.empty();//사용자 찾눈다
        if (nickname != null) u.setNickname(nickname);//닉네임 변경
        if (profileImage != null) u.setProfileImage(profileImage);//프로필이미지 변경
        return Optional.of(u);
    }

    // 회원삭제
    public boolean deleteById(Long id) {
        return USERS.remove(id) != null;
    }

    // 비밀번호 변경
    public Optional<User> updatePassword(Long id, String newPassword) {
        User u = USERS.get(id);
        if (u == null) return Optional.empty();
        u.setPassword(newPassword);
        return Optional.of(u);
    }
}
