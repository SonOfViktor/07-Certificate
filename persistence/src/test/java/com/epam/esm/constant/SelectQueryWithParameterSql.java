package com.epam.esm.constant;

public class SelectQueryWithParameterSql {
    public static final String QUERY_WITH_PARAMS_1 = """
        SELECT gift_certificate_id, gift_certificate.name, description,
               price, duration, create_date, last_update_date
        FROM module_two.gift_certificate
        JOIN module_two.gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
        JOIN module_two.tag ON gct_tag_id = tag_id
        WHERE tag.name = ? AND (gift_certificate.name LIKE ? OR description LIKE ?)
        ORDER BY gift_certificate.name ASC
    """;

    public static final String QUERY_WITH_PARAMS_2 = """
        SELECT gift_certificate_id, gift_certificate.name, description,
               price, duration, create_date, last_update_date
        FROM module_two.gift_certificate
        JOIN module_two.gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
        JOIN module_two.tag ON gct_tag_id = tag_id
        WHERE tag.name = ? AND (gift_certificate.name LIKE ? OR description LIKE ?)
        ORDER BY create_date DESC
    """;

    public static final String QUERY_WITH_PARAMS_3 = """
        SELECT gift_certificate_id, gift_certificate.name, description,
               price, duration, create_date, last_update_date
        FROM module_two.gift_certificate
        JOIN module_two.gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
        JOIN module_two.tag ON gct_tag_id = tag_id
        WHERE tag.name = ? AND gift_certificate.name ILIKE ?
    """;

    public static final String QUERY_WITH_PARAMS_4 = """
        SELECT gift_certificate_id, gift_certificate.name, description,
               price, duration, create_date, last_update_date
        FROM module_two.gift_certificate
        WHERE gift_certificate.name ILIKE ?
        ORDER BY gift_certificate.name ASC, create_date ASC
    """;
}
