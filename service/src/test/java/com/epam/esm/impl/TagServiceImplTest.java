package com.epam.esm.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    Set<Tag> tagSet;

    @InjectMocks
    TagServiceImpl tagService;

    @Mock
    TagDao tagDao;

    @BeforeAll
    void beforeAll() {
        tagSet = Set.of(new Tag(1, "food"), new Tag("summer"));
    }

    @Test
    void testAddTag() {
        Tag tag = new Tag();
        when(tagDao.createTag(tag)).thenReturn(1);

        int actual = tagService.addTag(tag);

        assertEquals(1, actual);
    }

    @Test
    void testAddExistedTag() {
        Tag tag = new Tag();
        when(tagDao.createTag(tag)).thenReturn(0);

        assertThrows(ResourceNotFoundException.class, () -> tagService.addTag(tag));
    }

    @Test
    void testAddTags() {
        when(tagDao.addTags(tagSet)).thenReturn(new int[]{0, 1});

        int[] actual = tagService.addTags(tagSet);

        assertArrayEquals(new int[]{0, 1}, actual);
    }

    @Test
    void testAddNullTags() {
        int[] actual = tagService.addTags(null);

        assertArrayEquals(new int[0], actual);
    }

    @Test
    void testFindAllTags() {
        when(tagDao.readAllTag()).thenReturn(tagSet);

        Set<Tag> actual = tagService.findAllTags();

        assertEquals(tagSet, actual);
    }

    @Test
    void testFindTagsByCertificateId() {
        when(tagDao.readAllTagByCertificateId(1)).thenReturn(tagSet);

        Set<Tag> actual = tagService.findTagsByCertificateId(1);

        assertEquals(tagSet, actual);
    }

    @Test
    void testFindTagById() {
        Tag tag = new Tag();
        when(tagDao.readTag(1)).thenReturn(Optional.of(tag));

        Tag actual = tagService.findTagById(1);

        assertEquals(tag, actual);
    }

    @Test
    void testNotFindTagById() {
        when(tagDao.readTag(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tagService.findTagById(1));
    }

    @Test
    void testDeleteTag() {
        when(tagDao.deleteTag(1)).thenReturn(1);

        int actual = tagService.deleteTag(1);

        assertEquals(1, actual);
    }

    @Test
    void testNotDeleteTag() {
        when(tagDao.deleteTag(1)).thenReturn(0);

        assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTag(1));
    }
}