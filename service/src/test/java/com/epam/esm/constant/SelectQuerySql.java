package com.epam.esm.constant;

public class SelectQuerySql {
    public static final String ALL_PARAMETERS_QUERY = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
                JOIN gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
                JOIN tag ON gct_tag_id = tag_id
                WHERE tag.name = ? AND (gift_certificate.name LIKE ? OR description LIKE ?)
                ORDER BY gift_certificate.name DESC, create_date ASC
            """;
    public static final String QUERY_WITHOUT_ORDER_CREATE_DATE = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
                JOIN gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
                JOIN tag ON gct_tag_id = tag_id
                WHERE tag.name = ? AND (gift_certificate.name LIKE ? OR description LIKE ?)
                ORDER BY gift_certificate.name DESC
            """;
    public static final String QUERY_WITHOUT_ORDER = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
                JOIN gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
                JOIN tag ON gct_tag_id = tag_id
                WHERE tag.name = ? AND (gift_certificate.name LIKE ? OR description LIKE ?)
            """;
    public static final String QUERY_WITHOUT_NAME = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
                JOIN gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
                JOIN tag ON gct_tag_id = tag_id
                WHERE tag.name = ? AND description LIKE ?
                ORDER BY gift_certificate.name DESC
            """;
    public static final String QUERY_WITHOUT_NAME_AND_DESCRIPTION = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
                JOIN gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
                JOIN tag ON gct_tag_id = tag_id
                WHERE tag.name = ?\s
                ORDER BY create_date ASC
            """;
    public static final String QUERY_WITHOUT_TAG = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
                WHERE (gift_certificate.name LIKE ? OR description LIKE ?)
                ORDER BY gift_certificate.name ASC
            """;
    public static final String QUERY_WITHOUT_DESCRIPTION_AND_TAG = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
                WHERE gift_certificate.name LIKE ?\s
                ORDER BY gift_certificate.name DESC
            """;
    public static final String QUERY_WITHOUT_PARAMETERS = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
            """;
}
