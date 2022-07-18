package com.epam.esm.dto;

import com.epam.esm.validategroup.ForCreate;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record CertificateTagsDto(
        int giftCertificateId,

        @NotBlank(groups =ForCreate.class)
        @Size(min = 3, max = 45)
        String name,

        @NotBlank(groups = ForCreate.class)
        @Size(max = 500)
        String description,

        @Positive
        @Digits(integer = 3, fraction = 2)
        BigDecimal price,

        @Positive(groups = ForCreate.class)
        @Digits(integer = 2, fraction = 0)
        int duration,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_JSON_PATTERN)
        LocalDateTime createDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_JSON_PATTERN)
        LocalDateTime lastUpdateDate,

        @Valid
        Set<TagDto> tags) {

    private static final String DATE_JSON_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
}
