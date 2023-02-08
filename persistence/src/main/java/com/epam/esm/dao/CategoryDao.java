package com.epam.esm.dao;

import com.epam.esm.entity.Category;
import org.springframework.data.repository.Repository;
import java.util.List;
import java.util.Optional;

public interface CategoryDao extends Repository<Category, Integer> {
    List<Category> findAll();

    Optional<Category> findByName(String name);
}
