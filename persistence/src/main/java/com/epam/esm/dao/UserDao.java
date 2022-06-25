package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface provided methods to control data in database table related with users.
 */
public interface UserDao extends JpaRepository<User, Integer> {
}
