package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
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
        when(giftCertificateDao.readAllCertificate()).thenReturn(certificateList);

        List<GiftCertificate> actual = giftCertificateService.findAllCertificates();
        assertEquals(certificateList, actual);
    }

    @Test
    void testFindCertificatesWithParams() {
        SelectQueryParameter params =  new SelectQueryParameter(List.of("food"), "e",
                "e", null, null);

        when(giftCertificateDao.readGiftCertificateWithParam(params))
                .thenReturn(certificateList);

        List<GiftCertificate> actual = giftCertificateService.findCertificatesWithParams(params);

        assertEquals(certificateList, actual);
    }

    @Test
    void testFindCertificatesWithNullParams() {
        SelectQueryParameter params = new SelectQueryParameter(null, null, null, null, null);

        when(giftCertificateDao.readGiftCertificateWithParam(params))
                .thenReturn(certificateList);

        List<GiftCertificate> actual = giftCertificateService.findCertificatesWithParams(params);

        assertEquals(certificateList, actual);
    }

    @Test
    void testNotFindCertificatesWithParams() {
        SelectQueryParameter params = new SelectQueryParameter(null,null, null, null, null);
        when(giftCertificateDao.readGiftCertificateWithParam(params))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> giftCertificateService.findCertificatesWithParams(params));
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