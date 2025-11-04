/*package com.example.restapi_demo.user.repository;

import com.example.restapi_demo.user.model.User;
import com.example.restapi_demo.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

//@Repository
public class InMemoryUserRepository implements UserRepository {


    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return store.values().stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst();
    }

    @Override
    public User save(User u) {

        Long id = (u.getId() == null) ? sequence.getAndIncrement() : u.getId();
        User saved = new User(id, u.getEmail(), u.getPassword(), u.getNickname(), u.getProfileImage());
        store.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> updateProfile(Long id, String nickname, String profileImage) {
        User u = store.get(id);
        if (u == null) return Optional.empty();
        if (nickname != null) u.setNickname(nickname);
        if (profileImage != null) u.setProfileImage(profileImage);
        store.put(id, u);
        return Optional.of(u);
    }

    @Override
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }

    @Override
    public Optional<User> updatePassword(Long id, String newPassword) {
        User u = store.get(id);
        if (u == null) return Optional.empty();
        u.setPassword(newPassword);
        store.put(id, u);
        return Optional.of(u);
    }
}
*/