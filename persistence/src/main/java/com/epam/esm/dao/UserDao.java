package com.epam.esm.dao;

import com.epam.esm.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    /**
     * Read all users from database
     *
     * @return list with all users from database
     */
    List<User> readAllUser();

    /**
     * Read user from database with specified id
     *
     * @param id the id of a user
     * @return the optional with read user from database if it exists or optional empty otherwise
     */
    Optional<User> readUserById(int id);
}
