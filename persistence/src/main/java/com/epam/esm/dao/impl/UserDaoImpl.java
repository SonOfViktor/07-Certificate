package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    public static final String SELECT_ALL_USERS_HQL = "select u from User u";
    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> readAllUser() {
        return entityManager.createQuery(SELECT_ALL_USERS_HQL, User.class).getResultList();
    }

    @Override
    public Optional<User> readUserById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }
}
