package com.epam.esm.service;

import com.epam.esm.entity.Page;
import com.epam.esm.entity.User;

public interface UserService {
    /**
     * Find specified page with users
     *
     * @param page number of page with users
     * @param size amount objects in one page
     * @return page with users
     */
    Page<User> findAllUser(int page, int size);
}
