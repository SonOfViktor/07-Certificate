package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.PageMeta;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public Page<User> findAllUser(int page, int size) {
        int userTotalElements = userDao.countUser();
        int totalPages = (int) Math.ceil((double) userTotalElements / size);

        if(page > totalPages) {
            throw new ResourceNotFoundException("There is no users in the database for " + page + " page");
        }

        PageMeta pageMeta = new PageMeta(size, userTotalElements, totalPages, page);

        int offset = page * size - size;
        List<User> users = userDao.readAllUser(offset, size);

        return new Page<>(users, pageMeta);
    }
}
