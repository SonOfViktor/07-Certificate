package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
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
import javax.persistence.EntityExistsException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    List<Tag> tags;

    @InjectMocks
    TagServiceImpl tagService;

    @Mock
    TagDao tagDao;

    @BeforeEach
    void init() {
        tags = List.of(Tag.builder().tagId(1).name("food").build(),
                Tag.builder().tagId(2).name("summer").build());
    }

    @Test
    void testAddTag() {
        TagDto tagDto = new TagDto(0, "new");
        Tag tag = Tag.builder().name("new").build();
        Tag expected = Tag.builder().tagId(1).name("new").build();

        when(tagDao.save(tag)).thenReturn(expected);

        Tag actual = tagService.addTag(tagDto);

        assertEquals(expected, actual);
    }

    @Test
    void testAddExistedTag() {
        TagDto tagDto = new TagDto(0, "new");
        Tag tag = Tag.builder().name("new").build();
        when(tagDao.existsByName(tag.getName())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> tagService.addTag(tagDto));
    }

    @Test
    void testAddTags() {
        Set<TagDto> tagDtoSet = Set.of(new TagDto(0, "new"));
        Tag tag = Tag.builder().name("new").build();
        Tag createdTag = Tag.builder().tagId(1).name("new").build();

        when(tagDao.save(tag)).thenReturn(createdTag);
        when(tagDao.findOneByName("new")).thenReturn(Optional.empty());

        Set<Tag> expected = Set.of(createdTag);
        Set<Tag> actual = tagService.addTags(tagDtoSet);

        assertEquals(expected, actual);
    }

    @Test
    void testAddNullTags() {
        Set<Tag> actual = tagService.addTags(null);

        assertEquals(Collections.emptySet(), actual);
    }

    @Test
    void testFindAllTags() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(tagDao.findAll(pageable)).thenReturn(new PageImpl<>(tags, pageable, 2));

        Page<Tag> expected = new PageImpl<>(tags, pageable, 2);
        Page<Tag> actual = tagService.findAllTags(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void testFindTagsByCertificateId() {
        when(tagDao.findAllByGiftCertificatesId(1)).thenReturn(new HashSet<>(tags));

        Set<Tag> actual = tagService.findTagsByCertificateId(1);

        assertEquals(new HashSet<>(tags), actual);
    }

    @Test
    void testFindTagsByCertificateIdWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        when(tagDao.findAllByGiftCertificatesId(2, pageable)).thenReturn(new PageImpl<>(tags, pageable, 2));

        Page<Tag> expected = new PageImpl<>(tags, pageable, 2);
        Page<Tag> actual = tagService.findTagsByCertificateId(2, pageable);

        assertEquals(expected, actual);
    }

    @Test
    void testFindTagById() {
        Tag tag = new Tag();
        when(tagDao.findById(1)).thenReturn(Optional.of(tag));

        Tag actual = tagService.findTagById(1);

        assertEquals(tag, actual);
    }

    @Test
    void testNotFindTagById() {
        when(tagDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tagService.findTagById(1));
    }

    @Test
    void testFindMostPopularHighestPriceTag() {
        when(tagDao.readMostPopularHighestPriceTag()).thenReturn(tags);

        List<Tag> actual = tagService.findMostPopularHighestPriceTag();

        assertEquals(tags, actual);
    }

    @Test
    void testDeleteTag() {
        tagService.deleteTag(1);

        verify(tagDao).deleteById(1);
    }
}