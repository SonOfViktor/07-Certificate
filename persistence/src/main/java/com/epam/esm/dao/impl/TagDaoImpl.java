package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String INSERT_TAG_SQL = """
            INSERT INTO module_two.tag (name) VALUES (:name)
            ON DUPLICATE KEY UPDATE  name = name
            """;
    private static final String READ_ALL_TAGS_BY_CERTIFICATE_ID = """
            SELECT tag_id, tag.name FROM module_two.tag
            JOIN module_two.gift_certificate_tag ON tag_id = gct_tag_id
            JOIN module_two.gift_certificate ON gct_gift_certificate_id = gift_certificate_id
            WHERE gift_certificate_id = ?
            """;
    private static final String READ_ALL_TAGS_SQL = """
            SELECT tag_id, name FROM module_two.tag
            """;
    private static final String READ_TAG_SQL = """
            SELECT tag_id, name FROM module_two.tag
            WHERE tag_id = ?
            """;
    private static final String DELETE_TAG_SQL = """
            DELETE FROM module_two.tag WHERE tag_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int createTag(Tag tag) {
        var source = new BeanPropertySqlParameterSource(tag);
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        KeyHolder holder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(INSERT_TAG_SQL, source, holder);
        Optional<Number> tagId = Optional.ofNullable(holder.getKey());

        return tagId.orElse(0).intValue();
    }

    @Override
    public int[] addTags(Set<Tag> tags) {
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        SqlParameterSource[] beans = tags.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        int[] affectedRows = namedParameterJdbcTemplate.batchUpdate(INSERT_TAG_SQL, beans);

        return affectedRows;
    }

    @Override
    public Set<Tag> readAllTag() {
        List<Tag> tags = jdbcTemplate.query(READ_ALL_TAGS_SQL, new BeanPropertyRowMapper<>(Tag.class));

        return Set.copyOf(tags);
    }

    @Override
    public Set<Tag> readAllTagByCertificateId(int certificateId) {
        List<Tag> tags = jdbcTemplate.query(READ_ALL_TAGS_BY_CERTIFICATE_ID,
                                            new BeanPropertyRowMapper<>(Tag.class), certificateId);

        return Set.copyOf(tags);
    }

    @Override
    public Optional<Tag> readTag(int id) {
        return jdbcTemplate
                .query(READ_TAG_SQL, new BeanPropertyRowMapper<>(Tag.class), id)
                .stream()
                .findFirst();
    }

    @Override
    public int deleteTag(int id) {
        int affectedRow = jdbcTemplate.update(DELETE_TAG_SQL, id);

        return affectedRow;
    }
}
