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
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TagDaoImplTest {
    public static final String MODULE_TWO_TAG = "module_3.tags";
    private TagDao tagDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImplTest(TagDao tagDao, JdbcTemplate jdbcTemplate) {
        this.tagDao = tagDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    void testTableRowQuantity() {
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, MODULE_TWO_TAG);
        int expected = 6;
        assertEquals(expected, actual);
    }

    @Order(0)
    @Test
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
        List<Tag> actual = tagDao.readAllTag();
        assertEquals(expected, actual);
    }

    @Test
    void testReadAllTagByCertificateId() {
        Set<Tag> expected = Set.of(new Tag(1,"food"), new Tag(2,"stationery"),
                new Tag(5,"paper"), new Tag(6,"by"));
        Set<Tag> actual = tagDao.readAllTagByCertificateId(3);
        assertEquals(expected, actual);
    }

    @Test
    void testReadAllTagByNonexistentCertificateId() {
        Set<Tag> actual = tagDao.readAllTagByCertificateId(22);
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
        List<Tag> expected = List.of(new Tag(2, "stationery"), new Tag(6, "by"));
        List<Tag> actual = tagDao.readMostPopularHighestPriceTag();

        assertEquals(expected, actual);
    }

    @Test
    void testDeleteTag() {
        tagDao.deleteTag(6);
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