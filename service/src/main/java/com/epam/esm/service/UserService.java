package com.epam.esm.service;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface provided methods to manage business logic related with users.
 */
public interface UserService {
    /**
     * Find specified page with users
     *
     * @param pageable the pageable to request a paged result, can be Pageable.unpaged(), must not be null.
     * @return page with users
     */
    Page<User> findAllUser(Pageable pageable);
}
