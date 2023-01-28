package com.epam.esm.service.impl;

import com.epam.esm.dao.CategoryDao;
import com.epam.esm.entity.Category;
import com.epam.esm.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private ImageService imageService;

    @Test
    void testFindAll() {
        List<Category> categories = List.of(new Category(), new Category());
        when(categoryDao.findAll()).thenReturn(categories);

        List<Category> actual = categoryService.findAll();

        assertThat(actual).isEqualTo(categories);
    }

    @Test
    void testFindByName() {
        when(categoryDao.findByName("name")).thenReturn(Optional.of(new Category()));

        Category expected = new Category();
        Category actual = categoryService.findByName("name");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testFindByNonExistName() {
        when(categoryDao.findByName("name")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findByName("name")).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testFindCategoryImage() {
        Category category = Category.builder().picture("test/image.jpg").build();
        when(categoryDao.findByName("name")).thenReturn(Optional.of(category));
        when(imageService.getImage("test/image.jpg")).thenReturn(Optional.of(new byte[] {0, 0, 1}));

        byte[] expected = {0, 0, 1};
        Optional<byte[]> actual = categoryService.findCategoryImage("name");

        assertThat(actual).contains(expected);
    }
}