package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.epam.esm.entity.SelectQueryOrder.ASC;
import static com.epam.esm.entity.SelectQueryOrder.DESC;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GiftCertificateDaoImplTest {
    public static final String GIFT_CERTIFICATE_TABLE = "module_3.gift_certificates";
    public static final String GIFT_CERTIFICATE_TAG_TABLE = "module_3.gift_certificate_tag";
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    public GiftCertificateDaoImplTest(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Test
    void testTableRowQuantity() {
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, GIFT_CERTIFICATE_TABLE);
        int expected = 4;
        assertEquals(expected, actual);
    }

    @Order(0)
    @Test
    void testCreateGiftCertificate() {
        GiftCertificate certificate = new GiftCertificate.GiftCertificateBuilder()
                .setName("Ganna")
                .setDescription("Хозяйки нам доверяют")
                .setPrice(new BigDecimal(60))
                .setDuration(30)
                .createGiftCertificate();

        int actualId = giftCertificateDao.createGiftCertificate(certificate).getGiftCertificateId();
        int expectedId = 5;

        assertEquals(expectedId, actualId);
    }

    @Order(1)
    @Test
    void testCreateGiftCertificateWithTags() {
        Set<Tag> tags = Set.of(new Tag("name"), new Tag("food"));
        tagDao.addTags(tags);
        GiftCertificate certificate = new GiftCertificate.GiftCertificateBuilder()
                .setName("Ganna")
                .setDescription("Хозяйки нам доверяют")
                .setPrice(new BigDecimal(60))
                .setDuration(30)
                .setTags(tags)
                .createGiftCertificate();

        int actualId = giftCertificateDao.createGiftCertificate(certificate).getGiftCertificateId();
        int expectedId = 6;

        entityManager.flush();

        int actualTableRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, GIFT_CERTIFICATE_TAG_TABLE);
        int expectedTableRows = 15;

        assertEquals(expectedId, actualId);
        assertEquals(expectedTableRows, actualTableRows);
    }

    @Test
    void testReadAllCertificate() {
        List<GiftCertificate> expected = createAllCertificateList();
        List<GiftCertificate> actual = giftCertificateDao.readAllCertificate();
        assertEquals(expected, actual);
    }

    @ParameterizedTest()
    @MethodSource("stringQueryAndResult")
    void testReadGiftCertificateWithParam(SelectQueryParameter parameter, List<GiftCertificate> expected) {
        List<GiftCertificate> actual = giftCertificateDao.readGiftCertificateWithParam(parameter);
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

    @ParameterizedTest()
    @MethodSource("updatingDataAndResult")
    void testUpdateGiftCertificate(GiftCertificate updatingCertificate, GiftCertificate expected) {
        GiftCertificate actual = giftCertificateDao.updateGiftCertificate(updatingCertificate).get();
        expected.setLastUpdateDate(actual.getLastUpdateDate());

        assertEquals(expected, actual);
    }

    @Test
    void testUpdateGiftCertificateWithTags() {
        Tag newTag = new Tag("name");
        tagDao.createTag(newTag);
        GiftCertificate certificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(1)
                .setTags(new HashSet<>(tagDao.readAllTagByCertificateId(1)))
                .createGiftCertificate();
        certificate.getTags().add(newTag);

        giftCertificateDao.updateGiftCertificate(certificate).get();

        entityManager.flush();

        int actualTableRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, GIFT_CERTIFICATE_TAG_TABLE);
        int expectedTableRows = 14;

        assertEquals(expectedTableRows, actualTableRows);
    }

    @Test
    void testUpdateNonExistentGiftCertificate() {
        GiftCertificate replaceCertificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(6)
                .setName("Milavitsa")
                .setDuration(40)
                .createGiftCertificate();
        assertTrue(giftCertificateDao.updateGiftCertificate(replaceCertificate).isEmpty());
    }

    @Test
    void testDeleteGiftCertificate() {
        int expectedAffectedRow = 1;
        int expectedCountRow = 3;

        int actualAffectedRow = giftCertificateDao.deleteGiftCertificate(3);
        int actualCountRow = JdbcTestUtils.countRowsInTable(jdbcTemplate, GIFT_CERTIFICATE_TABLE);

        assertEquals(expectedAffectedRow, actualAffectedRow);
        assertEquals(expectedCountRow, actualCountRow);
    }

    @Test
    void testDeleteNonexistentGiftCertificate() {
        int expectedAffectedRow = 0;
        int expectedCountRow = 4;

        int actualAffectedRow = giftCertificateDao.deleteGiftCertificate(6);
        int actualCountRow = JdbcTestUtils.countRowsInTable(jdbcTemplate, GIFT_CERTIFICATE_TABLE);

        assertEquals(expectedAffectedRow, actualAffectedRow);
        assertEquals(expectedCountRow, actualCountRow);
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
                arguments(new SelectQueryParameter(List.of("paper"), "e", "two", ASC, null),
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1)
                                .sorted(Comparator.comparing(GiftCertificate::getName))
                                .toList()),
                arguments(new SelectQueryParameter(List.of("paper"), "e", "two", null, DESC),
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1)
                                .sorted(Comparator.comparing(GiftCertificate::getCreateDate).reversed())
                                .toList()),
                arguments(new SelectQueryParameter(List.of("paper", "stationery", "by"), "e", null, null, DESC),
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 2)
                                .sorted(Comparator.comparing(GiftCertificate::getCreateDate).reversed())
                                .toList()),
                arguments(new SelectQueryParameter(List.of("paper"), "e", null, null, null),
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1)
                                .sorted(Comparator.comparing(GiftCertificate::getName))
                                .toList()),
                arguments(new SelectQueryParameter(null, "e", null, ASC, ASC),
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1)
                                .sorted(Comparator.comparing(GiftCertificate::getName).thenComparing(GiftCertificate::getCreateDate))
                                .toList()),
                arguments(new SelectQueryParameter(Collections.emptyList(), "e", null, ASC, ASC),
                        createAllCertificateList().stream()
                                .filter(certificate -> certificate.getGiftCertificateId() > 1)
                                .sorted(Comparator.comparing(GiftCertificate::getName).thenComparing(GiftCertificate::getCreateDate))
                                .toList())
        );
    }

    Stream<Arguments> updatingDataAndResult() {
        GiftCertificate notUpdatedCertificate = createAllCertificateList().get(0);

        return Stream.of(
                arguments(new GiftCertificate.GiftCertificateBuilder()
                                .setGiftCertificateId(1)
                                .setName("   ")
                                .setDescription("   ")
                                .setPrice(null)
                                .setDuration(-8)
                                .createGiftCertificate(), notUpdatedCertificate),
                arguments(new GiftCertificate.GiftCertificateBuilder()
                                .setGiftCertificateId(1)
                                .createGiftCertificate(), notUpdatedCertificate),
                arguments(new GiftCertificate.GiftCertificateBuilder()
                                .setGiftCertificateId(1)
                                .setName("  Milavitsa ")
                                .setDescription(" Our care for you")
                                .setPrice(new BigDecimal("15.15"))
                                .setDuration(10)
                                .createGiftCertificate(),
                        new GiftCertificate.GiftCertificateBuilder()
                                .setGiftCertificateId(1)
                                .setName("Milavitsa")
                                .setDescription("Our care for you")
                                .setPrice(new BigDecimal("15.15"))
                                .setDuration(10)
                                .setCreateDate(notUpdatedCertificate.getCreateDate())
                                .createGiftCertificate()),
                arguments(new GiftCertificate.GiftCertificateBuilder()
                                .setGiftCertificateId(1)
                                .setPrice(new BigDecimal(20))
                                .setDuration(99)
                                .createGiftCertificate(),
                        new GiftCertificate.GiftCertificateBuilder()
                                .setGiftCertificateId(1)
                                .setName("Oz.by")
                                .setDescription("Books, games, stationery")
                                .setPrice(new BigDecimal("20"))
                                .setDuration(99)
                                .setCreateDate(LocalDateTime.of(2022, 4, 11, 13, 48, 14, 0))
                                .createGiftCertificate())
        );
    }
}