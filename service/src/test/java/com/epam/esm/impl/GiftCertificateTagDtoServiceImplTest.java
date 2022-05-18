package com.epam.esm.impl;


import com.epam.esm.dao.impl.GiftCertificateTagDaoImpl;
import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.impl.GiftCertificateTagDtoServiceImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GiftCertificateTagDtoServiceImplTest {
    private List<GiftCertificate> giftCertificateList;
    private List<CertificateTagsDto> certificateTagsDtoList;
    private Set<Tag> tagSet;

    @InjectMocks
    private GiftCertificateTagDtoServiceImpl giftCertificateTagDtoService;

    @Mock
    private GiftCertificateTagDaoImpl giftCertificateTagDao;

    @Mock
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private TagServiceImpl tagService;

    @BeforeAll
    void beforeAll() {
        giftCertificateList = List.of(new GiftCertificate(), new GiftCertificate());
        giftCertificateList.get(0).setGiftCertificateId(1);
        giftCertificateList.get(1).setGiftCertificateId(2);

        tagSet = Set.of(new Tag("food"), new Tag("summer"));

        certificateTagsDtoList = giftCertificateList.stream()
                .map((cert) -> new CertificateTagsDto(cert, tagSet))
                .toList();
    }

    @Test
    void testAddGiftCertificateTagDto() {
        CertificateTagsDto certificateTagsDto = new CertificateTagsDto(new GiftCertificate(), Set.of());
        when(giftCertificateService.addGiftCertificate(certificateTagsDto.certificate())).thenReturn(1);

        int actual = giftCertificateTagDtoService.addGiftCertificateTagDto(certificateTagsDto);
        verify(tagService, times(1)).addTags(certificateTagsDto.tags());
        verify(giftCertificateTagDao, times(1)).createGiftCertificateTagEntries(actual, certificateTagsDto.tags());

        assertEquals(1, actual);
    }

    @Test
    void testAddGiftCertificateTagDtoNullTags() {
        CertificateTagsDto certificateTagsDto = new CertificateTagsDto(new GiftCertificate(), null);
        when(giftCertificateService.addGiftCertificate(certificateTagsDto.certificate())).thenReturn(1);

        int actual = giftCertificateTagDtoService.addGiftCertificateTagDto(certificateTagsDto);

        verify(tagService, never()).addTags(certificateTagsDto.tags());
        verify(giftCertificateTagDao, never()).createGiftCertificateTagEntries(actual, certificateTagsDto.tags());
        assertEquals(1, actual);
    }

    @Test
    void testFindAllGiftCertificateTagDto() {
        when(giftCertificateService.findAllCertificates()).thenReturn(giftCertificateList);
        when(tagService.findTagsByCertificateId(anyInt())).thenReturn(tagSet);

        List<CertificateTagsDto> actual = giftCertificateTagDtoService.findAllGiftCertificateTagDto();

        assertEquals(certificateTagsDtoList, actual);
    }

    @Test
    void testFindGiftCertificateTagDtoByParam() {
        SelectQueryParameter selectQueryParameter = new SelectQueryParameter(null, null, null, null, null);
        when(giftCertificateService.findCertificatesWithParams(selectQueryParameter)).thenReturn(giftCertificateList);
        when(tagService.findTagsByCertificateId(anyInt())).thenReturn(tagSet);

        List<CertificateTagsDto> actual = giftCertificateTagDtoService
                .findGiftCertificateTagDtoByParam(selectQueryParameter);

        assertEquals(certificateTagsDtoList, actual);
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
        when(giftCertificateTagDao.createGiftCertificateTagEntries(anyInt(), eq(tagSet))).thenReturn(new int[] {1, 0, 1});

        int[] actual = giftCertificateTagDtoService.updateGiftCertificateTagDto(certificateTagsDtoList.get(0), 1);

        assertArrayEquals(new int[] {1, 0, 1}, actual);
    }

    @Test
    void testUpdateGiftCertificateTagDtoNullTags() {
        CertificateTagsDto certificateTagsDto = new CertificateTagsDto(new GiftCertificate(), null);

        int[] actual = giftCertificateTagDtoService.updateGiftCertificateTagDto(certificateTagsDto, 1);

        verify(tagService, never()).addTags(certificateTagsDto.tags());
        verify(giftCertificateTagDao, never()).createGiftCertificateTagEntries(1, certificateTagsDto.tags());
        assertArrayEquals(new int[0], actual);
    }


}