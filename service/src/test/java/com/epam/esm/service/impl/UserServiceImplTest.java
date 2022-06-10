package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.PageMeta;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserDao userDao;

    List<User> users;

    @BeforeEach
    void init() {
        users = List.of(
                new User(1, "Margo", "Singer"),
                new User(2, "Andrey", "Spider")
        );
    }

    @Test
    void testFindAllUser() {
        when(userDao.countUser()).thenReturn(2);
        when(userDao.readAllUser(0, 10)).thenReturn(users);

        Page<User> expected = new Page<>(users, new PageMeta(10, 2, 1, 1));
        Page<User> actual = userService.findAllUser(1, 10);

        assertEquals(expected, actual);
    }

    @Test
    void testFindAllUserOnNonExistentPage() {
        when(userDao.countUser()).thenReturn(2);

        assertThrows(ResourceNotFoundException.class, () -> userService.findAllUser(3, 7));
    }
}