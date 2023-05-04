package com.ljy.jwt.domain;

import java.util.Optional;

public interface UserRepository {
    boolean existByUserId(String id);
    void save(User user);

    Optional<User> findByUserId(String userId);
}
