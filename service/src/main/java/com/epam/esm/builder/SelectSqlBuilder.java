package com.epam.esm.builder;

import com.epam.esm.entity.SelectQueryParameter;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.*;

@Component
public class SelectSqlBuilder {
    private static final String BEGIN_SELECT_SQL = """
                SELECT gift_certificate_id, gift_certificate.name, description,
                       price, duration, create_date, last_update_date
                FROM gift_certificate
            """;
    private static final String SELECT_JOIN_PART = """
                JOIN gift_certificate_tag ON gift_certificate_id = gct_gift_certificate_id
                JOIN tag ON gct_tag_id = tag_id
            """;
    private static final String WHERE_PART_PATTERN = """
                WHERE %s%s%s%s%s
            """;
    private static final String WHERE_PART_PATTERN_WITH_BRACES = """
                WHERE %s%s(%s%s%s)
            """;
    private static final String ORDER_PART_PATTERN = """
                ORDER BY %s%s%s
            """;
    private static final String TAG_NAME = "tag.name = ? ";
    private static final String CERTIFICATE_NAME = "gift_certificate.name LIKE ? ";
    private static final String CERTIFICATE_DESCRIPTION = "description LIKE ?";
    private static final String OR = "OR ";
    private static final String AND = "AND ";
    private static final String COMMA = ", ";
    private static final String ORDER_NAME = "gift_certificate.name ";
    private static final String ORDER_DATE = "create_date ";

    public String buildSelectGiftCertificateSQL(SelectQueryParameter params) {
        StringBuilder querySql = new StringBuilder(BEGIN_SELECT_SQL);

        if (isAllFieldNullOrBlank(params)) {
            return querySql.toString();
        }

        String tagName = EMPTY;
        if (!isBlank(params.tagName())) {
            tagName = TAG_NAME;
            querySql.append(SELECT_JOIN_PART);
        }

        querySql.append(createQueryWherePart(params, tagName));
        querySql.append(createQueryOrderPart(params));

        return querySql.toString();
    }

    private String createQueryWherePart(SelectQueryParameter params, String tagName) {
        String queryWherePart = EMPTY;
        String or = EMPTY;
        String pattern = WHERE_PART_PATTERN;

        String name = !isBlank(params.certificateName()) ? CERTIFICATE_NAME : EMPTY;
        String description = !isBlank(params.certificateDescription()) ? CERTIFICATE_DESCRIPTION : EMPTY;

        if(isNoneEmpty(name, description)) {
            or = OR;
            pattern = WHERE_PART_PATTERN_WITH_BRACES;
        }

        String and = (!tagName.isEmpty() && !isAllEmpty(name, description)) ? AND : EMPTY;

        if (!isAllEmpty(name, description, tagName)) {
            queryWherePart = String.format(pattern, tagName, and, name, or, description);
        }

        return queryWherePart;
    }

    private String createQueryOrderPart(SelectQueryParameter params) {
        String queryOrderPart = EMPTY;

        String orderName = params.orderName() != null ? ORDER_NAME + params.orderName() : EMPTY;
        String orderDate = params.orderDate() != null ? ORDER_DATE + params.orderDate() : EMPTY;
        String orderComma = (isNoneEmpty(orderName, orderDate)) ? COMMA : EMPTY;

        if (!isAllEmpty(orderName, orderDate)) {
            queryOrderPart = String.format(ORDER_PART_PATTERN, orderName, orderComma, orderDate);
        }

        return queryOrderPart;
    }

    private boolean isAllFieldNullOrBlank(SelectQueryParameter params) {
        return isAllBlank(params.tagName(), params.certificateName(), params.certificateDescription()) &&
                params.orderName() == null && params.orderDate() == null;
    }
}
