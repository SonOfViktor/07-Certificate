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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
class TagDaoImplTest {
    public static final String MODULE_TWO_TAG = "module_two.tag";
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
        int actual = tagDao.createTag(tag);
        assertEquals(7, actual);
    }

    @Test
    void testCreateTagSameName() {
        Tag tag = new Tag("food");
        int actual = tagDao.createTag(tag);
        assertEquals(0, actual);
    }

    @Order(1)
    @Test
    void addTags() {
        List<Tag> tags = List.of(new Tag("food"), new Tag("business"), new Tag("shopping"));
        long actual = tagDao.addTags(tags);
        assertEquals(2, actual);
    }

    @Test
    void addAllExistTags() {
        List<Tag> tags = List.of(new Tag("food"), new Tag("shoe"), new Tag("paper"));
        long actual = tagDao.addTags(tags);
        assertEquals(0, actual);
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
        List<Tag> expected = List.of(new Tag(1,"food"), new Tag(2,"stationery"),
                new Tag(5,"paper"), new Tag(6,"by"));
        List<Tag> actual = tagDao.readAllTagByCertificateId(3);
        assertEquals(expected, actual);
    }

    @Test
    void testReadAllTagByNonexistentCertificateId() {
        List<Tag> actual = tagDao.readAllTagByCertificateId(22);
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