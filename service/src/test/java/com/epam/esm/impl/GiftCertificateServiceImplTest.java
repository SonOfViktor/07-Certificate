package com.epam.esm.impl;

import com.epam.esm.builder.SelectSqlBuilder;
import com.epam.esm.constant.SelectQuerySql;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GiftCertificateServiceImplTest {
    private List<GiftCertificate> certificateList;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Spy
    private SelectSqlBuilder builder;

    @BeforeAll
    void beforeAll() {
        certificateList = List.of(new GiftCertificate(), new GiftCertificate());
    }

    @Test
    void testAddGiftCertificate() {
        when(giftCertificateDao.createGiftCertificate(new GiftCertificate())).thenReturn(1);
        int actual = giftCertificateService.addGiftCertificate(new GiftCertificate());

        assertEquals(1, actual);
    }

    @Test
    void testFindAllCertificates() {
        when(giftCertificateDao.readAllCertificate()).thenReturn(certificateList);

        List<GiftCertificate> actual = giftCertificateService.findAllCertificates();
        assertEquals(certificateList, actual);
    }

    @Test
    void testFindCertificatesWithParams() {
        SelectQueryParameter parameter =  new SelectQueryParameter("food", "e",
                "e", null, null);

        when(giftCertificateDao
                .readGiftCertificateWithParam(eq(SelectQuerySql.QUERY_WITHOUT_ORDER), eq(List.of("food", "%e%", "%e%"))))
                .thenReturn(certificateList);

        List<GiftCertificate> actual = giftCertificateService.findCertificatesWithParams(parameter);

        assertEquals(certificateList, actual);
    }

    @Test
    void testFindCertificatesWithNullParams() {
        when(giftCertificateDao
                .readGiftCertificateWithParam(eq(SelectQuerySql.QUERY_WITHOUT_PARAMETERS), eq(List.of())))
                .thenReturn(certificateList);

        List<GiftCertificate> actual = giftCertificateService
                .findCertificatesWithParams(new SelectQueryParameter(null, null, null, null, null));

        assertEquals(certificateList, actual);
    }

    @Test
    void testNotFindCertificatesWithParams() {
        SelectQueryParameter selectQueryParameter = new SelectQueryParameter(null,null, null, null, null);
        when(giftCertificateDao
                .readGiftCertificateWithParam(eq(SelectQuerySql.QUERY_WITHOUT_PARAMETERS), eq(List.of())))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> giftCertificateService.findCertificatesWithParams(selectQueryParameter));
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
        when(giftCertificateDao.updateGiftCertificate(certificate)).thenReturn(1);

        int actual = giftCertificateService.updateGiftCertificate(certificate, 1);

        assertEquals(1, actual);
    }

    @Test
    void testUpdateNotExistGiftCertificate() {
        GiftCertificate certificate = certificateList.get(0);
        when(giftCertificateDao.updateGiftCertificate(certificate)).thenReturn(0);

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