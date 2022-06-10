package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> readUserById(int id);
}
