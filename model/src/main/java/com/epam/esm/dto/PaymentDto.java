package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PaymentDto(
        int id,
        String userName,
        List<UserOrderDto> userOrderDtoList,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_JSON_PATTERN)
        LocalDateTime createdDate
) {
    private static final String DATE_JSON_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static record UserOrderDto(String certificateName, BigDecimal cost) {
    }
}
