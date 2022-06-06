package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserDaoImplTest {
    public static final String USER_TABLE = "module_3.users";
    private final UserDao userDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImplTest(UserDao userDao, JdbcTemplate jdbcTemplate) {
        this.userDao = userDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    void testTableRowQuantity() {
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE);
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    void testReadAllUser() {
        List<User> expected = List.of(new User(1, "Ivan", "Pupkin"),
                new User(2, "Sanek", "Lupkin"));
        List<User> actual = userDao.readAllUser();

        assertEquals(expected, actual);
    }

    @Test
    void testReadUserById() {
        User expected = new User(1, "Ivan", "Pupkin");
        User actual = userDao.readUserById(1).get();

        assertEquals(expected, actual);
    }

    @Test
    void testReadNonExistUserById() {
        Optional<User> actual = userDao.readUserById(8);

        assertEquals(Optional.empty(), actual);
    }
}
