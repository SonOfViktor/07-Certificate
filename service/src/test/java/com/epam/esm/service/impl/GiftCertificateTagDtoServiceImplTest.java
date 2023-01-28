package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateTagsCreateEditDto;
import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Category;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateFilter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CategoryService;
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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateTagDtoServiceImplTest {
    private List<GiftCertificate> giftCertificateList;
    private List<CertificateTagsDto> certificateTagsDtoList;
    private Set<Tag> tagSet;

    @InjectMocks
    private GiftCertificateTagDtoServiceImpl giftCertificateTagDtoService;

    @Mock
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private TagServiceImpl tagService;

    @BeforeEach
    void init() {
        Set<TagDto> tagDtoSet = Set.of(new TagDto(1, "tag1"),
                new TagDto(2, "tag2"));
        tagSet = Set.of(Tag.builder().tagId(1).name("tag1").build(),
                Tag.builder().tagId(2).name("tag2").build());

        giftCertificateList = List.of(GiftCertificate.builder().giftCertificateId(1).name("name1")
                        .description("description1").duration(10).price(new BigDecimal("10"))
                        .image("test1.jpg").category(Category.builder().name("Sport").build()).tags(tagSet).build(),
                GiftCertificate.builder().giftCertificateId(2).name("name2").description("description2")
                        .duration(20).price(new BigDecimal("20")).image("test2.jpg")
                        .category(Category.builder().name("Food").build()).build());

        certificateTagsDtoList = List.of(new CertificateTagsDto(1, "name1", "description1", new BigDecimal("10"), 10,
                        "test1.jpg", "Sport", null, null, tagDtoSet),
                new CertificateTagsDto(2, "name2", "description2", new BigDecimal("20"), 20,
                        "test2.jpg", "Food", null, null, Collections.emptySet()));
    }

    @Test
    void testAddGiftCertificateTagDto() {
        CertificateTagsCreateEditDto certificateTagsCreateEditDto = new CertificateTagsCreateEditDto("name1",
                "description1", new BigDecimal("10"), 10, null, "Sport",
                Set.of("tag1", "tag2"));
        GiftCertificate certificate = GiftCertificate.builder().name("name1")
                .description("description1")
                .duration(10)
                .price(new BigDecimal("10"))
                .build();
        GiftCertificate savedCertificate = giftCertificateList.get(0);

        when(giftCertificateService.addGiftCertificate(any(GiftCertificate.class), any())).thenReturn(savedCertificate);
        when(tagService.addTags(Set.of("tag1", "tag2"))).thenReturn(tagSet);
        when(categoryService.findByName("Sport")).thenReturn(Category.builder().name("Sport").build());

        CertificateTagsDto expected = certificateTagsDtoList.get(0);
        CertificateTagsDto actual = giftCertificateTagDtoService.addGiftCertificateTagDto(certificateTagsCreateEditDto, null);

        verify(giftCertificateService).addGiftCertificate(argThat(cert -> cert.equals(certificate)
                && cert.getTags().equals(tagSet)
                && cert.getCategory().equals(Category.builder().name("Sport").build())), any());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testAddGiftCertificateTagDtoNullTags() {
        CertificateTagsCreateEditDto certificateTagsDto = new CertificateTagsCreateEditDto( "name2", "description2",
                new BigDecimal("20"), 20, null, "Food", null);
        GiftCertificate savedCertificate = giftCertificateList.get(1);

        when(giftCertificateService.addGiftCertificate(any(GiftCertificate.class), any())).thenReturn(savedCertificate);
        when(tagService.addTags(certificateTagsDto.tags())).thenReturn(Collections.emptySet());
        when(categoryService.findByName("Food")).thenReturn(Category.builder().name("Food").build());

        CertificateTagsDto expected = new CertificateTagsDto(2, "name2", "description2",
                new BigDecimal("20"), 20, "test2.jpg", "Food", null, null, Collections.emptySet());
        CertificateTagsDto actual = giftCertificateTagDtoService.addGiftCertificateTagDto(certificateTagsDto, null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testFindAllGiftCertificateTagDto() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<GiftCertificate> page = new PageImpl<>(giftCertificateList, pageable, 2);

        when(giftCertificateService.findAllCertificates(pageable)).thenReturn(page);

        Page<CertificateTagsDto> expected = new PageImpl<>(certificateTagsDtoList, pageable, 2);
        Page<CertificateTagsDto> actual = giftCertificateTagDtoService.findAllGiftCertificateTagDto(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void testFindGiftCertificateTagDtoByParam() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<GiftCertificate> page = new PageImpl<>(giftCertificateList, pageable, 2);
        GiftCertificateFilter giftCertificateFilter = new GiftCertificateFilter(null, null, null, null);

        when(giftCertificateService.findCertificatesWithParams(giftCertificateFilter, pageable)).thenReturn(page);

        Page<CertificateTagsDto> expected = new PageImpl<>(certificateTagsDtoList, pageable, 2);
        Page<CertificateTagsDto> actual = giftCertificateTagDtoService
                .findGiftCertificateTagDtoByParam(giftCertificateFilter, pageable);

        assertEquals(expected, actual);
    }

    @Test
    void testFindGiftCertificateTagDto() {
        when(giftCertificateService.findCertificateById(anyInt())).thenReturn(giftCertificateList.get(0));

        CertificateTagsDto actual = giftCertificateTagDtoService.findGiftCertificateTagDto(1);

        assertEquals(certificateTagsDtoList.get(0), actual);
    }

    @Test
    void testUpdateGiftCertificateTagDto() {
        CertificateTagsCreateEditDto certificateTagsCreateEditDto = new CertificateTagsCreateEditDto("name1",
                "description1", new BigDecimal("10"), 10, null, "Sport",
                Set.of("tag1", "tag2"));
        GiftCertificate certificate = GiftCertificate.builder().name("name1")
                .description("description1")
                .duration(10)
                .price(new BigDecimal("10"))
                .build();

        when(tagService.addTags(Set.of("tag1", "tag2"))).thenReturn(tagSet);
        when(giftCertificateService.updateGiftCertificate(1, certificate, Optional.empty()))
                .thenReturn(giftCertificateList.get(1));

        CertificateTagsDto expected = certificateTagsDtoList.get(1);
        CertificateTagsDto actual = giftCertificateTagDtoService
                .updateGiftCertificateTagDto(1, certificateTagsCreateEditDto, Optional.empty());

        assertEquals(expected, actual);
    }

    @Test
    void testUpdateGiftCertificateTagDtoWithoutCategoryAndDuration() {
        CertificateTagsCreateEditDto certificateTagsCreateEditDto = new CertificateTagsCreateEditDto("name1",
                "description1", new BigDecimal("10"), null, null, null,
                Set.of("tag1", "tag2"));
        GiftCertificate certificate = GiftCertificate.builder().name("name1")
                .description("description1")
                .duration(0)
                .price(new BigDecimal("10"))
                .build();

        when(tagService.addTags(Set.of("tag1", "tag2"))).thenReturn(tagSet);
        when(giftCertificateService.updateGiftCertificate(1, certificate, Optional.empty()))
                .thenReturn(giftCertificateList.get(1));

        CertificateTagsDto expected = certificateTagsDtoList.get(1);
        CertificateTagsDto actual = giftCertificateTagDtoService
                .updateGiftCertificateTagDto(1, certificateTagsCreateEditDto, Optional.empty());

        assertEquals(expected, actual);
    }
}