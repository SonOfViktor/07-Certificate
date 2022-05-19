package com.epam.esm.dao.impl;

import com.epam.esm.constant.SelectQueryWithParameterSql;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GiftCertificateDaoImplTest {
    private GiftCertificateDao giftCertificateDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDaoImplTest(GiftCertificateDao giftCertificateDao, JdbcTemplate jdbcTemplate) {
        this.giftCertificateDao = giftCertificateDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    void testTableRowQuantity() {
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "module_two.gift_certificate");
        int expected = 4;
        assertEquals(expected, actual);
    }

    @Rollback
    @Test
    void testCreateGiftCertificate() {
        GiftCertificate certificate = new GiftCertificate.GiftCertificateBuilder()
                .setName("Ganna")
                .setDescription("Хозяйки нам доверяют")
                .setPrice(new BigDecimal(60))
                .setDuration(30)
                .createGiftCertificate();
        int actualId = giftCertificateDao.createGiftCertificate(certificate);
        int expectedId = 5;
        assertEquals(expectedId, actualId);
    }

    @Test
    void testReadAllCertificate() {
        List<GiftCertificate> expected = createAllCertificateList();
        List<GiftCertificate> actual = giftCertificateDao.readAllCertificate();
        assertEquals(expected, actual);
    }

    @ParameterizedTest()
    @MethodSource("stringQueryAndResult")
    void testReadGiftCertificateWithParam(String sql, List<GiftCertificate> expected, List<String> args) {
        List<GiftCertificate> actual = giftCertificateDao.readGiftCertificateWithParam(sql, args);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void testReadGiftCertificate(int id) {
        GiftCertificate expected = createAllCertificateList().get(id - 1);
        GiftCertificate actual = giftCertificateDao.readGiftCertificate(id).get();
        assertEquals(expected, actual);
    }

    @Test
    void testReadNonexistentGiftCertificate() {
        Optional<GiftCertificate> actual = giftCertificateDao.readGiftCertificate(10);
        assertThrows(NoSuchElementException.class, actual::get);
    }

    @Rollback
    @Test
    @Disabled("H2 don't support sql syntax of MySql :(")
    void testUpdateGiftCertificate() {
        GiftCertificate replaceCertificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(1)
                .setName("Milavitsa")
                .setDescription("Our care for you")
                .setDuration(40)
                .createGiftCertificate();
        int actual = giftCertificateDao.updateGiftCertificate(replaceCertificate);
        assertEquals(1, actual);
    }

    @Rollback
    @Test
    void testDeleteGiftCertificate() {
        giftCertificateDao.deleteGiftCertificate(3);
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "module_two.gift_certificate");
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Rollback
    @Test
    void testDeleteNonexistentGiftCertificate() {
        giftCertificateDao.deleteGiftCertificate(6);
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "module_two.gift_certificate");
        int expected = 4;
        assertEquals(expected, actual);
    }

    @AfterAll
    void shutDownDataBase() {
        DataSource dataSource = jdbcTemplate.getDataSource();
        if (dataSource instanceof EmbeddedDatabase embeddedDatabase) {
            embeddedDatabase.shutdown();
        }
    }

    private List<GiftCertificate> createAllCertificateList() {
        return List.of(
                new GiftCertificate.GiftCertificateBuilder().setGiftCertificateId(1).setName("Oz.by")
                        .setDescription("Books, games, stationery").setPrice(new BigDecimal("20.00"))
                        .setDuration(40)
                        .setCreateDate(LocalDateTime.of(2022, 4, 11, 13, 48, 14, 0))
                        .setLastUpdateDate(LocalDateTime.of(2022, 4, 11, 13, 48, 14, 0))
                        .createGiftCertificate(),
                new GiftCertificate.GiftCertificateBuilder().setGiftCertificateId(2).setName("Belvest")
                        .setDescription("Change two shoes").setPrice(new BigDecimal("50.00"))
                        .setDuration(30)
                        .setCreateDate(LocalDateTime.of(2022, 4, 12, 13, 48, 14, 0))
                        .setLastUpdateDate(LocalDateTime.of(2022, 4, 12, 13, 48, 14, 0))
                        .createGiftCertificate(),
                new GiftCertificate.GiftCertificateBuilder().setGiftCertificateId(3).setName("Evroopt")
                        .setDescription("We have a lot of sugar!").setPrice(new BigDecimal("40.00"))
                        .setDuration(10)
                        .setCreateDate(LocalDateTime.of(2022, 4, 13, 13, 48, 14, 0))
                        .setLastUpdateDate(LocalDateTime.of(2022, 4, 13, 13, 48, 14, 0))
                        .createGiftCertificate(),
                new GiftCertificate.GiftCertificateBuilder().setGiftCertificateId(4).setName("Evroopt")
                        .setDescription("Buy two bananas").setPrice(new BigDecimal("20.00"))
                        .setDuration(10)
                        .setCreateDate(LocalDateTime.of(2022, 4, 10, 13, 48, 14, 0))
                        .setLastUpdateDate(LocalDateTime.of(2022, 4, 10, 13, 48, 14, 0))
                        .createGiftCertificate());
    }

    Stream<Arguments> stringQueryAndResult() {
        return Stream.of(
                arguments(SelectQueryWithParameterSql.QUERY_WITH_PARAMS_1,
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1 && certificate.getGiftCertificateId() != 3)
                                .sorted(Comparator.comparing(GiftCertificate::getName))
                                .toList(), List.of("paper", "%e%", "%two%")),
                arguments(SelectQueryWithParameterSql.QUERY_WITH_PARAMS_2,
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1 && certificate.getGiftCertificateId() != 3)
                                .sorted(Comparator.comparing(GiftCertificate::getName))
                                .toList(), List.of("paper", "%e%", "%two%")),
                arguments(SelectQueryWithParameterSql.QUERY_WITH_PARAMS_3,
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1)
                                .sorted(Comparator.comparing(GiftCertificate::getName))
                                .toList(), List.of("paper", "%e%")),
                arguments(SelectQueryWithParameterSql.QUERY_WITH_PARAMS_4,
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1)
                                .sorted(Comparator.comparing(GiftCertificate::getName).thenComparing(GiftCertificate::getCreateDate))
                                .toList(), List.of("%e%"))
        );
    }
}