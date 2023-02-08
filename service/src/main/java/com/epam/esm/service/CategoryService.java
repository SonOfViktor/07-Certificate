package com.epam.esm.service;

import com.epam.esm.entity.Category;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface CategoryService {
    /**
     * Find all categories.
     *
     * @return all categories
     */
    List<Category> findAll();

    /**
     * Find category with specified name
     *
     * @param name the name of category in database
     * @return the category with specified name
     * @throws NoSuchElementException if category with specified name wasn't found
     */
    Category findByName(String name);

    /**
     * Find picture of category with specified name
     *
     * @param name the id of category in database
     * @return the byte array that contains the picture of category with specified name
     */
    Optional<byte[]> findCategoryImage(String name);
}