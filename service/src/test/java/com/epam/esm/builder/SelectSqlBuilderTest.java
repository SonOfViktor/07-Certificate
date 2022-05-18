package com.epam.esm.builder;

import com.epam.esm.constant.SelectQuerySql;
import com.epam.esm.entity.SelectQueryOrder;
import com.epam.esm.entity.SelectQueryParameter;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class SelectSqlBuilderTest {
    private SelectSqlBuilder sqlBuilder;

    @Autowired
    public SelectSqlBuilderTest(SelectSqlBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    @ParameterizedTest()
    @MethodSource("selectQueryParametersAndResult")
    void testBuildSelectGiftCertificateSQL(SelectQueryParameter parameter, String expectedSql) {
        String actualSql = sqlBuilder.buildSelectGiftCertificateSQL(parameter);

        assertEquals(expectedSql, actualSql);
    }

    Stream<Arguments> selectQueryParametersAndResult() {
        return Stream.of(
                arguments(new SelectQueryParameter("tag", "name", "description",
                        SelectQueryOrder.DESC, SelectQueryOrder.ASC),
                        SelectQuerySql.ALL_PARAMETERS_QUERY),
                arguments(new SelectQueryParameter("tag", "name", "description",
                        SelectQueryOrder.DESC, null),
                        SelectQuerySql.QUERY_WITHOUT_ORDER_CREATE_DATE),
                arguments(new SelectQueryParameter("tag", "name", "description",
                                null, null),
                        SelectQuerySql.QUERY_WITHOUT_ORDER),
                arguments(new SelectQueryParameter("tag", "", "description",
                                SelectQueryOrder.DESC, null),
                        SelectQuerySql.QUERY_WITHOUT_NAME),
                arguments(new SelectQueryParameter("tag", "", null,
                                null, SelectQueryOrder.ASC),
                        SelectQuerySql.QUERY_WITHOUT_NAME_AND_DESCRIPTION),
                arguments(new SelectQueryParameter("", "name", "description",
                                SelectQueryOrder.ASC, null),
                        SelectQuerySql.QUERY_WITHOUT_TAG),
                arguments(new SelectQueryParameter("", "name", null,
                                SelectQueryOrder.DESC, null),
                        SelectQuerySql.QUERY_WITHOUT_DESCRIPTION_AND_TAG),
                arguments(new SelectQueryParameter("", "", null,
                                null, null),
                        SelectQuerySql.QUERY_WITHOUT_PARAMETERS)
        );
    }
}