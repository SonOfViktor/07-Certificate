package com.epam.esm.service.impl;

import com.epam.esm.dao.CategoryDao;
import com.epam.esm.entity.Category;
import com.epam.esm.service.CategoryService;
import com.epam.esm.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;
    private final ImageService imageService;

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public Category findByName(String name) {
        return categoryDao.findByName(name)
                .orElseThrow();
    }

    @Override
    public Optional<byte[]> findCategoryImage(String name) {
        return categoryDao.findByName(name)
                .map(Category::getPicture)
                .filter(StringUtils::isNotBlank)
                .flatMap(imageService::getImage);
    }
}