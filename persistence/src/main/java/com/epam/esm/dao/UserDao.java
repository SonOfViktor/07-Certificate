package com.epam.esm.dao;

import com.epam.esm.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    /**
     * Read users from database
     *
     * @param offset initial offset in table with users
     * @param size amount entries to extract from table with users
     * @return list with users from database
     */
    List<User> readAllUser(int offset, int size);

    /**
     * Read user from database with specified id
     *
     * @param id the id of a user
     * @return the optional with read user from database if it exists or optional empty otherwise
     */
    Optional<User> readUserById(int id);

    /**
     * Find out amount of entries in table with users
     *
     * @return amount of entries in table with users
     */
    int countUser();
}
