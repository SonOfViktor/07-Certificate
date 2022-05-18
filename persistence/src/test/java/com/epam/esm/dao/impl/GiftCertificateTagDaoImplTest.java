package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GiftCertificateTagDaoImplTest {
    private GiftCertificateTagDao giftCertificateTagDao;
    private TagDao tagDao;
    private GiftCertificateDao giftCertificateDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateTagDaoImplTest(GiftCertificateTagDao giftCertificateTagDao, TagDao tagDao,
                                         GiftCertificateDao giftCertificateDao, JdbcTemplate jdbcTemplate) {
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    @Rollback
    @Disabled("H2 don't support sql syntax of MySql :(")
    void testCreateGiftCertificateTagEntries() {
        Set<Tag> tags = Set.of(new Tag(1, "food"),
                new Tag(3, "shoe"), new Tag(5, "paper"));
        giftCertificateTagDao.createGiftCertificateTagEntries(1, tags);
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "gift_certificate_tag");
        int expected = 17;
        assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void testDeleteGiftCertificateCascade() {
        giftCertificateDao.deleteGiftCertificate(1);
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "gift_certificate_tag");
        int expected = 11;
        assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void testDeleteTagCascade() {
        tagDao.deleteTag(6);
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "gift_certificate_tag");
        int expected = 10;
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