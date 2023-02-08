package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record CertificateTagsDto(
        int giftCertificateId,

        String name,

        String description,

        BigDecimal price,

        int duration,

        String image,

        String category,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_JSON_PATTERN)
        LocalDateTime createDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_JSON_PATTERN)
        LocalDateTime lastUpdateDate,

        Set<TagDto> tags
) {
    private static final String DATE_JSON_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
}
