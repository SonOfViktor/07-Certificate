package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String GIFT_CERTIFICATE_TABLE = "gift_certificate";
    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String PRICE_COLUMN = "price";
    private static final String DURATION_COLUMN = "duration";
    private static final String GIFT_CERTIFICATE_ID_COLUMN = "gift_certificate_id";
    private static final String READ_ALL_CERTIFICATE_SQL = """
            SELECT gift_certificate_id, gift_certificate.name, description, price, duration, create_date,
                last_update_date
            FROM gift_certificate
            """;
    private static final String READ_CERTIFICATE_BY_ID_SQL = """
            SELECT gift_certificate_id, gift_certificate.name, description, price, duration, create_date,
                last_update_date
            FROM gift_certificate
            WHERE gift_certificate_id = ?
            """;
    private static final String UPDATE_CERTIFICATE_BY_ID_SQL = """
            UPDATE gift_certificate
            SET name = IF (TRIM(:name) <> '', :name, name),
            	description = IF (TRIM(:description) IS NOT NULL, :description, description),
                price = IF (TRIM(:price) <> '', :price, price),
                duration = IF (:duration > 0, :duration, duration),
                last_update_date = NOW()
            WHERE gift_certificate_id = :giftCertificateId
            """;
    private static final String DELETE_CERTIFICATE_SQL = """
            DELETE FROM gift_certificate
            WHERE gift_certificate_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int createGiftCertificate(GiftCertificate certificate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(GIFT_CERTIFICATE_TABLE)
                .usingColumns(NAME_COLUMN, DESCRIPTION_COLUMN, PRICE_COLUMN, DURATION_COLUMN)
                .usingGeneratedKeyColumns(GIFT_CERTIFICATE_ID_COLUMN)
                .executeAndReturnKey(new BeanPropertySqlParameterSource(certificate)).intValue();
    }

    @Override
    public List<GiftCertificate> readAllCertificate() {
        List<GiftCertificate> giftCertificates;

        giftCertificates = jdbcTemplate
                .query(READ_ALL_CERTIFICATE_SQL, new BeanPropertyRowMapper<>(GiftCertificate.class));

        return giftCertificates;
    }

    @Override
    public List<GiftCertificate> readGiftCertificateWithParam(String sql, List<String> args) {
        List<GiftCertificate> giftCertificates;

        giftCertificates = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(GiftCertificate.class), args.toArray());

        return giftCertificates;
    }

    @Override
    public Optional<GiftCertificate> readGiftCertificate(int id) {
        Optional<GiftCertificate> giftCertificates;

        giftCertificates = jdbcTemplate
                .query(READ_CERTIFICATE_BY_ID_SQL, new BeanPropertyRowMapper<>(GiftCertificate.class), id)
                .stream()
                .findFirst();

        return giftCertificates;
    }

    @Override
    public int updateGiftCertificate(GiftCertificate certificate) {
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        var beanParameterSource = new BeanPropertySqlParameterSource(certificate);

        int affectedRow = namedParameterJdbcTemplate.update(UPDATE_CERTIFICATE_BY_ID_SQL, beanParameterSource);

        return affectedRow;
    }

    @Override
    public int deleteGiftCertificate(int id) {
        int affectedRow = jdbcTemplate.update(DELETE_CERTIFICATE_SQL, id);

        return affectedRow;
    }
}
