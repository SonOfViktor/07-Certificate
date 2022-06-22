package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    public static final String SELECT_ALL_USERS_HQL = "select u from User u order by u.userId";
    public static final String SELECT_USER_COUNT_HQL = "select count(u) from User u";
    private final EntityManager entityManager;

    @Override
    public List<User> readAllUser(int offset, int size) {
        return entityManager.createQuery(SELECT_ALL_USERS_HQL, User.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Optional<User> readUserById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public int countUser() {
        return entityManager.createQuery(SELECT_USER_COUNT_HQL, Long.class)
                .getSingleResult()
                .intValue();
    }
}
