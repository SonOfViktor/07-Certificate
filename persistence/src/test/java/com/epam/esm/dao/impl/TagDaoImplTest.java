package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TagDaoImplTest {
    public static final String MODULE_TWO_TAG = "module_4.tags";
    private TagDao tagDao;
    private JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    public TagDaoImplTest(TagDao tagDao, JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.tagDao = tagDao;
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Test
    void testTableRowQuantity() {
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, MODULE_TWO_TAG);
        int expected = 6;
        assertEquals(expected, actual);
    }

    @Order(0)
    @Test
    @Disabled("GiftCertificateDaoTest impact on id of tags")
    void testCreateTag() {
        Tag tag = new Tag("piece");
        int actual = tagDao.createTag(tag).getTagId();
        assertEquals(7, actual);
    }

    @Test
    void testCreateTagSameName() {
        Tag tag = new Tag("food");
        int actual = tagDao.createTag(tag).getTagId();
        assertEquals(1, actual);
    }

    @Order(1)
    @Test
    @Disabled("GiftCertificateDaoTest impact on id of tags")
    void addTags() {
        Set<Tag> tags = Set.of(new Tag("food"), new Tag("business"), new Tag("shopping"));

        int[] expected = new int[]{1, 8, 9};
        int[] actual = tagDao.addTags(tags).stream().map(Tag::getTagId).mapToInt(id -> id).toArray();

        assertThat(actual).contains(expected);
    }

    @Test
    void addAllExistTags() {
        Set<Tag> tags = Set.of(new Tag("food"), new Tag("shoe"), new Tag("paper"));

        int[] expected = new int[]{1, 5, 3};
        int[] actual = tagDao.addTags(tags).stream().map(Tag::getTagId).mapToInt(id -> id).toArray();

        assertThat(actual).contains(expected);
    }

    @Test
    void testReadAllTag() {
        List<Tag> expected = List.of(new Tag(1,"food"), new Tag(2,"stationery"),
                new Tag(3,"shoe"), new Tag(4,"virtual"), new Tag(5,"paper"),
                new Tag(6,"by"));
        List<Tag> actual = tagDao.readAllTag(0, 10);
        assertEquals(expected, actual);
    }

    @Test
    void testReadAllTagWithPagination() {
        List<Tag> expected = List.of(new Tag(3,"shoe"), new Tag(4,"virtual"));
        List<Tag> actual = tagDao.readAllTag(2, 2);

        assertEquals(expected, actual);
    }

    @Test
    void testReadAllTagByCertificateId() {
        Set<Tag> expected = Set.of(new Tag(1,"food"), new Tag(2,"stationery"),
                new Tag(5,"paper"), new Tag(6,"by"));
        Set<Tag> actual = tagDao.readTagByCertificateId(3);
        assertEquals(expected, actual);
    }

    @Test
    void testReadAllTagByCertificateIdWithPagination() {
        Set<Tag> expected = Set.of(new Tag(5,"paper"), new Tag(6,"by"));
        Set<Tag> actual = tagDao.readTagByCertificateId(3, 2, 2);
        assertEquals(expected, actual);
    }

    @Test
    void testReadAllTagByNonexistentCertificateId() {
        Set<Tag> actual = tagDao.readTagByCertificateId(22);
        assertTrue(actual.isEmpty());
    }

    @Test
    void testReadTag() {
        Optional<Tag> actual = tagDao.readTag(25);
        assertTrue(actual.isEmpty());
    }

    @Test
    void testReadNonexistentTag() {
        Tag expected = new Tag(3, "shoe");
        Tag actual = tagDao.readTag(3).get();
        assertEquals(expected, actual);
    }

    @Test
    void testReadMostPopularHighestPriceTag() {
        List<Tag> expected = List.of(new Tag(2, "stationery"), new Tag(5, "paper"), new Tag(6, "by"));
        List<Tag> actual = tagDao.readMostPopularHighestPriceTag();

        assertEquals(expected, actual);
    }

    @Test
    void testCountTagsWithCertificateId() {
        Map<String, Integer> params = Map.of("id", 2);

        int actual = tagDao.countTags(params);

        assertEquals(3, actual);
    }

    @Test
    void testCountTagsWithEmptyParams() {
        int actual = tagDao.countTags(Collections.emptyMap());

        assertEquals(6, actual);
    }

    @Test
    void testCountTagsWithNullParams() {
        int actual = tagDao.countTags(null);

        assertEquals(6, actual);
    }

    @Test
    void testDeleteTag() {
        tagDao.deleteTag(6);
        entityManager.flush();
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, MODULE_TWO_TAG);
        int expected = 5;
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteNonexistentTag() {
        tagDao.deleteTag(7);
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, MODULE_TWO_TAG);
        int expected = 6;
        assertEquals(expected, actual);
    }

    @AfterAll
    void shutDownDataBase() {
        DataSource dataSource = jdbcTemplate.getDataSource();
        if (dataSource instanceof EmbeddedDatabase embeddedDatabase) {
            embeddedDatabase.shutdown();
        }
    }
}