package com.epam.esm.service;

import com.epam.esm.entity.User;

import java.util.List;

public interface UserService {
    /**
     * Find all users
     *
     * @return list with all users
     */
    List<User> findAllUser();
}
