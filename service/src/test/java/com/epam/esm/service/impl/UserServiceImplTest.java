package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserDao userDao;

    List<User> userList;
    Page<User> users;

    @BeforeEach
    void init() {
        Pageable pageable = PageRequest.of(1, 10);
        userList = List.of(new User(1, "Margo", "Singer"),
                new User(2, "Andrey", "Spider"));

        users = new PageImpl<>(userList, pageable, 2);
    }

    @Test
    void testFindAllUser() {
        Pageable pageable = PageRequest.of(1, 10);
        when(userDao.findAll(pageable)).thenReturn(users);

        Page<User> expected = new PageImpl<>(userList, PageRequest.of(1, 10), 2);
        Page<User> actual = userService.findAllUser(pageable);

        assertEquals(expected, actual);
    }
}