package com.ljy.jwt.infrastructure;

import com.ljy.jwt.domain.User;
import com.ljy.jwt.domain.UserRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.ljy.jwt.domain.QUser.user;

@Repository
public class QuerydslUserRepository extends QuerydslRepositorySupport implements UserRepository {
    public QuerydslUserRepository() {
        super(User.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existByUserId(String userId) {
        return from(user)
                .select(user.id)
                .where(user.userId.eq(userId))
                .fetchFirst() != null;
    }

    @Override
    @Transactional
    public void save(User user) {
        EntityManager entityManager = getEntityManager();
        if (entityManager.contains(user)) {
            entityManager.merge(user);
            return;
        }
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(
            from(user)
            .select(user)
            .where(user.userId.eq(userId))
            .fetchFirst()
        );
    }
}
