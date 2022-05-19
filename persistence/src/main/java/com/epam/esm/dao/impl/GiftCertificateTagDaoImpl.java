package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class GiftCertificateTagDaoImpl implements GiftCertificateTagDao {
    private static final String INSERT_ENTRY_SQL = """
                INSERT IGNORE INTO module_two.gift_certificate_tag (gct_gift_certificate_id, gct_tag_id)
                VALUES (%s, (SELECT tag_id FROM tag WHERE tag.name = :name))
            """;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateTagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int[] createGiftCertificateTagEntries(int certificateId, Set<Tag> tags) {
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        SqlParameterSource[] beans = tags.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        int[] affectedRow = namedParameterJdbcTemplate
                .batchUpdate(String.format(INSERT_ENTRY_SQL, certificateId), beans);

        return affectedRow;
    }
}
