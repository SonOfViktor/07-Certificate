package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.PageMeta;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
    private List<GiftCertificate> certificateList;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @BeforeEach
    void init() {
        certificateList = List.of(new GiftCertificate(), new GiftCertificate());
    }

    @Test
    void testAddGiftCertificate() {
        GiftCertificate expected = new GiftCertificate();
        when(giftCertificateDao.createGiftCertificate(new GiftCertificate())).thenReturn(expected);
        GiftCertificate actual = giftCertificateService.addGiftCertificate(expected);

        assertEquals(expected, actual);
    }

    @Test
    void testFindAllCertificates() {
        when(giftCertificateDao.readAllCertificate(0, 10)).thenReturn(certificateList);
        when(giftCertificateDao.countGiftCertificate(null)).thenReturn(2);
        Page<GiftCertificate> expected = new Page<>(certificateList, new PageMeta(10, 2, 1, 1));

        Page<GiftCertificate> actual = giftCertificateService.findAllCertificates(1, 10);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAllCertificatesOnNonExistentPage() {
        when(giftCertificateDao.countGiftCertificate(null)).thenReturn(2);

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.findAllCertificates(3, 10));
    }

    @Test
    void testFindCertificatesWithParams() {
        SelectQueryParameter params =  new SelectQueryParameter(List.of("food"), "e",
                "e", null, null);
        Page<GiftCertificate> expected = new Page<>(certificateList, new PageMeta(10, 2, 1, 1));

        when(giftCertificateDao.countGiftCertificate(params)).thenReturn(2);
        when(giftCertificateDao.readGiftCertificateWithParam(params, 0, 10))
                .thenReturn(certificateList);

        Page<GiftCertificate> actual = giftCertificateService.findCertificatesWithParams(params, 1, 10);

        assertEquals(expected, actual);
    }

    @Test
    void testFindAllCertificatesWithParamsOnNonExistentPage() {
        SelectQueryParameter params =  new SelectQueryParameter(List.of("food"), "e",
                "e", null, null);
        when(giftCertificateDao.countGiftCertificate(params)).thenReturn(2);

        assertThrows(ResourceNotFoundException.class, () ->
                giftCertificateService.findCertificatesWithParams(params, 3, 10));
    }

    @Test
    void testFindCertificateById() {
        GiftCertificate expected = certificateList.get(0);
        when(giftCertificateDao.readGiftCertificate(anyInt())).thenReturn(Optional.of(expected));

        GiftCertificate actual = giftCertificateService.findCertificateById(1);

        assertEquals(expected, actual);
    }

    @Test
    void testNotFindCertificateById() {
        when(giftCertificateDao.readGiftCertificate(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.findCertificateById(1));
    }

    @Test
    void testUpdateGiftCertificate() {
        GiftCertificate certificate = certificateList.get(0);
        when(giftCertificateDao.updateGiftCertificate(certificate)).thenReturn(Optional.of(certificate));

        GiftCertificate actual = giftCertificateService.updateGiftCertificate(certificate, 1);

        assertEquals(certificate, actual);
    }

    @Test
    void testUpdateNotExistGiftCertificate() {
        GiftCertificate certificate = certificateList.get(0);
        when(giftCertificateDao.updateGiftCertificate(certificate)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> giftCertificateService.updateGiftCertificate(certificate, 1));
    }

    @Test
    void testDeleteCertificate() {
        when(giftCertificateDao.deleteGiftCertificate(anyInt())).thenReturn(1);

        int actual = giftCertificateService.deleteCertificate(2);

        assertEquals(1, actual);
    }

    @Test
    void testDeleteNotExistCertificate() {
        when(giftCertificateDao.deleteGiftCertificate(anyInt())).thenReturn(0);

        assertThrows(ResourceNotFoundException.class,
                () -> giftCertificateService.deleteCertificate(2));
    }
}