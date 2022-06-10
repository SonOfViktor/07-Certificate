package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    Set<Tag> tags;

    @InjectMocks
    TagServiceImpl tagService;

    @Mock
    TagDao tagDao;

    @BeforeEach
    void init() {
        tags = Set.of(new Tag(1, "food"), new Tag("summer"));
    }

    @Test
    void testAddTag() {
        Tag tag = new Tag();
        when(tagDao.createTag(tag)).thenReturn(new Tag(1, ""));

        Tag expected = new Tag(1, "");
        Tag actual = tagService.addTag(tag);

        assertEquals(expected, actual);
    }

    @Test
    void testAddTags() {
        when(tagDao.addTags(tags)).thenReturn(Set.of(new Tag(1, "")));

        Set<Tag> expected = Set.of(new Tag(1, ""));
        Set<Tag> actual = tagService.addTags(tags);

        assertEquals(expected, actual);
    }

    @Test
    void testAddNullTags() {
        Set<Tag> actual = tagService.addTags(null);

        assertEquals(Collections.emptySet(), actual);
    }

    @Test
    void testFindAllTags() {
        when(tagDao.readAllTag()).thenReturn(new ArrayList<>(tags));

        List<Tag> actual = tagService.findAllTags();

        assertEquals(new ArrayList<>(tags), actual);
    }

    @Test
    void testFindTagsByCertificateId() {
        when(tagDao.readAllTagByCertificateId(1)).thenReturn(tags);

        Set<Tag> actual = tagService.findTagsByCertificateId(1);

        assertEquals(tags, actual);
    }

    @Test
    void testFindTagById() {
        Tag tag = new Tag();
        when(tagDao.readTag(1)).thenReturn(Optional.of(tag));

        Tag actual = tagService.findTagById(1);

        assertEquals(tag, actual);
    }

    @Test
    void testFind() {
        List<Tag> expected = List.of(new Tag(2, "stationery"), new Tag(6, "by"));
        when(tagDao.readMostPopularHighestPriceTag()).thenReturn(expected);

        List<Tag> actual = tagService.findMostPopularHighestPriceTag();

        assertEquals(actual, expected);
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