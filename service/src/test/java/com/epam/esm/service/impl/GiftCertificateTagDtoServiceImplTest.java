package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateFilter;
import com.epam.esm.entity.Tag;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateTagDtoServiceImplTest {
    private List<GiftCertificate> giftCertificateList;
    private List<CertificateTagsDto> certificateTagsDtoList;
    private Set<TagDto> tags;
    private Set<Tag> tagSet;

    @InjectMocks
    private GiftCertificateTagDtoServiceImpl giftCertificateTagDtoService;

    @Mock
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private TagServiceImpl tagService;

    @BeforeEach
    void init() {
        giftCertificateList = List.of(GiftCertificate.builder().giftCertificateId(1).name("name1")
                        .description("description1").duration(10).price(new BigDecimal("10")).build(),
                GiftCertificate.builder().giftCertificateId(2).name("name2").description("description2")
                        .duration(20).price(new BigDecimal("20")).build());

        tags = Set.of(new TagDto(1, "tag1"),
                new TagDto(2, "tag2"));
        tagSet = Set.of(Tag.builder().tagId(1).name("tag1").build(),
                Tag.builder().tagId(2).name("tag2").build());

        certificateTagsDtoList = List.of(new CertificateTagsDto(1, "name1", "description1", new BigDecimal("10"), 10,
                        null, null, tags),
                new CertificateTagsDto(2, "name2", "description2", new BigDecimal("20"), 20,
                        null, null, tags));
    }

    @Test
    void testAddGiftCertificateTagDto() {
        CertificateTagsDto certificateTagsDto = new CertificateTagsDto(0, "name1", "description1",
                new BigDecimal("10"), 10, null, null, tags);
        GiftCertificate certificate = GiftCertificate.builder().name("name1").description("description1")
                .duration(10).price(new BigDecimal("10")).build();
        GiftCertificate savedCertificate = giftCertificateList.get(0);

        when(giftCertificateService.addGiftCertificate(certificate)).thenReturn(savedCertificate);
        when(tagService.addTags(certificateTagsDto.tags())).thenReturn(tagSet);

        CertificateTagsDto expected = certificateTagsDtoList.get(0);
        CertificateTagsDto actual = giftCertificateTagDtoService.addGiftCertificateTagDto(certificateTagsDto);

        assertThat(actual).usingRecursiveComparison().ignoringFields("createDate", "lastUpdateDate").isEqualTo(expected);
    }

    @Test
    void testAddGiftCertificateTagDtoNullTags() {
        CertificateTagsDto certificateTagsDto = new CertificateTagsDto(0, "name1", "description1",
                new BigDecimal("10"), 10, null, null, null);
        GiftCertificate certificate = GiftCertificate.builder().name("name1").description("description1")
                .duration(10).price(new BigDecimal("10")).build();
        GiftCertificate savedCertificate = giftCertificateList.get(0);

        when(giftCertificateService.addGiftCertificate(certificate)).thenReturn(savedCertificate);
        when(tagService.addTags(certificateTagsDto.tags())).thenReturn(Collections.emptySet());

        CertificateTagsDto expected = new CertificateTagsDto(1, "name1", "description1",
                new BigDecimal("10"), 10, null, null, Collections.emptySet());
        CertificateTagsDto actual = giftCertificateTagDtoService.addGiftCertificateTagDto(certificateTagsDto);

        assertThat(actual).usingRecursiveComparison().ignoringFields("createDate", "lastUpdateDate").isEqualTo(expected);
    }

    @Test
    void testFindAllGiftCertificateTagDto() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<GiftCertificate> page = new PageImpl<>(giftCertificateList, pageable, 2);

        when(giftCertificateService.findAllCertificates(pageable)).thenReturn(page);
        when(tagService.findTagsByCertificateId(anyInt())).thenReturn(tagSet);

        Page<CertificateTagsDto> expected = new PageImpl<>(certificateTagsDtoList, pageable, 2);
        Page<CertificateTagsDto> actual = giftCertificateTagDtoService.findAllGiftCertificateTagDto(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void testFindGiftCertificateTagDtoByParam() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<GiftCertificate> page = new PageImpl<>(giftCertificateList, pageable, 2);
        GiftCertificateFilter giftCertificateFilter = new GiftCertificateFilter(null, null, null);

        when(giftCertificateService.findCertificatesWithParams(giftCertificateFilter, pageable)).thenReturn(page);
        when(tagService.findTagsByCertificateId(anyInt())).thenReturn(tagSet);

        Page<CertificateTagsDto> expected = new PageImpl<>(certificateTagsDtoList, pageable, 2);
        Page<CertificateTagsDto> actual = giftCertificateTagDtoService
                .findGiftCertificateTagDtoByParam(giftCertificateFilter, pageable);

        assertEquals(expected, actual);
    }

    @Test
    void testFindGiftCertificateTagDto() {
        when(giftCertificateService.findCertificateById(anyInt())).thenReturn(giftCertificateList.get(0));
        when(tagService.findTagsByCertificateId(anyInt())).thenReturn(tagSet);

        CertificateTagsDto actual = giftCertificateTagDtoService.findGiftCertificateTagDto(1);

        assertEquals(certificateTagsDtoList.get(0), actual);
    }

    @Test
    void testUpdateGiftCertificateTagDto() {
        CertificateTagsDto certificateTagsDto = new CertificateTagsDto(0, "name1", "description1",
                new BigDecimal("10"), 10, null, null, tags);
        GiftCertificate certificate = GiftCertificate.builder().name("name1").description("description1")
                .duration(10).price(new BigDecimal("10")).build();

        when(tagService.findTagsByCertificateId(anyInt())).thenReturn(tagSet);
        when(tagService.addTags(tags)).thenReturn(tagSet);
        when(giftCertificateService.updateGiftCertificate(certificate, 1))
                .thenReturn(giftCertificateList.get(1));

        CertificateTagsDto expected = certificateTagsDtoList.get(1);
        CertificateTagsDto actual = giftCertificateTagDtoService
                .updateGiftCertificateTagDto(certificateTagsDto, 1);

        assertEquals(expected, actual);
    }
}