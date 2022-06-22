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
    public static final String GIFT_CERTIFICATE_TABLE = "module_4.gift_certificates";
    public static final String GIFT_CERTIFICATE_TAG_TABLE = "module_4.gift_certificate_tag";
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
        GiftCertificate certificate = GiftCertificate.builder()
                .name("Ganna")
                .description("Хозяйки нам доверяют")
                .price(new BigDecimal(60))
                .duration(30)
                .build();

        int actualId = giftCertificateDao.createGiftCertificate(certificate).getGiftCertificateId();
        int expectedId = 5;

        assertEquals(expectedId, actualId);
    }

    @Order(1)
    @Test
    void testCreateGiftCertificateWithTags() {
        Set<Tag> tags = Set.of(new Tag(0, "name"), new Tag(0, "food"));
        tagDao.addTags(tags);
        GiftCertificate certificate = GiftCertificate.builder()
                .name("Ganna")
                .description("Хозяйки нам доверяют")
                .price(new BigDecimal(60))
                .duration(30)
                .tags(tags)
                .build();

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
        List<GiftCertificate> actual = giftCertificateDao.readAllCertificate(0, 5);
        assertEquals(expected, actual);
    }

    @Test
    void testReadCertificateWithPagination() {
        List<GiftCertificate> expected = createAllCertificateList().stream()
                .filter(c -> c.getGiftCertificateId() > 2)
                .toList();
        List<GiftCertificate> actual = giftCertificateDao.readAllCertificate(2, 5);
        assertEquals(expected, actual);
    }

    @ParameterizedTest()
    @MethodSource("stringQueryAndResult")
    void testReadGiftCertificateWithParam(SelectQueryParameter parameter, List<GiftCertificate> expected) {
        List<GiftCertificate> actual = giftCertificateDao.readGiftCertificateWithParam(parameter, 0, 5);
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

    @Test
    void testCountGiftCertificateWithoutParameters() {
        int actual = giftCertificateDao.countGiftCertificate(null);

        assertEquals(4, actual);
    }

    @Test
    void testCountGiftCertificateWithParameters() {
        SelectQueryParameter parameter =
                new SelectQueryParameter(List.of("paper"), "e", null, null, null);
        int actual = giftCertificateDao.countGiftCertificate(parameter);

        assertEquals(3, actual);
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
        Tag newTag = new Tag(0, "name");
        tagDao.createTag(newTag);
        GiftCertificate certificate = GiftCertificate.builder()
                .giftCertificateId(1)
                .tags(new HashSet<>(tagDao.readTagByCertificateId(1)))
                .build();
        certificate.getTags().add(newTag);

        giftCertificateDao.updateGiftCertificate(certificate).get();

        entityManager.flush();

        int actualTableRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, GIFT_CERTIFICATE_TAG_TABLE);
        int expectedTableRows = 14;

        assertEquals(expectedTableRows, actualTableRows);
    }

    @Test
    void testUpdateNonExistentGiftCertificate() {
        GiftCertificate replaceCertificate = GiftCertificate.builder()
                .giftCertificateId(6)
                .name("Milavitsa")
                .duration(40)
                .build();
        assertTrue(giftCertificateDao.updateGiftCertificate(replaceCertificate).isEmpty());
    }

    @Test
    void testDeleteGiftCertificate() {
        int expectedDeletedCertificateId = 3;
        int expectedCountRow = 3;

        int actualAffectedRow = giftCertificateDao.deleteGiftCertificate(3);
        entityManager.flush();
        int actualCountRow = JdbcTestUtils.countRowsInTable(jdbcTemplate, GIFT_CERTIFICATE_TABLE);

        assertEquals(expectedDeletedCertificateId, actualAffectedRow);
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
                GiftCertificate.builder().giftCertificateId(1).name("Oz.by")
                        .description("Books, games, stationery").price(new BigDecimal("20.00"))
                        .duration(40)
                        .createDate(LocalDateTime.of(2022, 4, 11, 13, 48, 14, 0))
                        .lastUpdateDate(LocalDateTime.of(2022, 4, 11, 13, 48, 14, 0))
                        .build(),
                GiftCertificate.builder().giftCertificateId(2).name("Belvest")
                        .description("Change two shoes").price(new BigDecimal("50.00"))
                        .duration(30)
                        .createDate(LocalDateTime.of(2022, 4, 12, 13, 48, 14, 0))
                        .lastUpdateDate(LocalDateTime.of(2022, 4, 12, 13, 48, 14, 0))
                        .build(),
                GiftCertificate.builder().giftCertificateId(3).name("Evroopt")
                        .description("We have a lot of sugar!").price(new BigDecimal("40.00"))
                        .duration(10)
                        .createDate(LocalDateTime.of(2022, 4, 13, 13, 48, 14, 0))
                        .lastUpdateDate(LocalDateTime.of(2022, 4, 13, 13, 48, 14, 0))
                        .build(),
                GiftCertificate.builder().giftCertificateId(4).name("Evroopt")
                        .description("Buy two bananas").price(new BigDecimal("20.00"))
                        .duration(10)
                        .createDate(LocalDateTime.of(2022, 4, 10, 13, 48, 14, 0))
                        .lastUpdateDate(LocalDateTime.of(2022, 4, 10, 13, 48, 14, 0))
                        .build());
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
                arguments(GiftCertificate.builder()
                                .giftCertificateId(1)
                                .name("   ")
                                .description("   ")
                                .price(null)
                                .duration(-8)
                                .build(), notUpdatedCertificate),
                arguments(GiftCertificate.builder()
                                .giftCertificateId(1)
                                .build(), notUpdatedCertificate),
                arguments(GiftCertificate.builder()
                                .giftCertificateId(1)
                                .name("  Milavitsa ")
                                .description(" Our care for you")
                                .price(new BigDecimal("15.15"))
                                .duration(10)
                                .build(),
                        GiftCertificate.builder()
                                .giftCertificateId(1)
                                .name("Milavitsa")
                                .description("Our care for you")
                                .price(new BigDecimal("15.15"))
                                .duration(10)
                                .createDate(notUpdatedCertificate.getCreateDate())
                                .build()),
                arguments(GiftCertificate.builder()
                                .giftCertificateId(1)
                                .price(new BigDecimal(20))
                                .duration(99)
                                .build(),
                        GiftCertificate.builder()
                                .giftCertificateId(1)
                                .name("Oz.by")
                                .description("Books, games, stationery")
                                .price(new BigDecimal("20"))
                                .duration(99)
                                .createDate(LocalDateTime.of(2022, 4, 11, 13, 48, 14, 0))
                                .build())
        );
    }
}