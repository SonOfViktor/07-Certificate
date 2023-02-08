package com.epam.esm.dto;

import com.epam.esm.validation.FileImage;
import com.epam.esm.validation.group.ForCreate;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

public record CertificateTagsCreateEditDto(
        @NotBlank(groups = ForCreate.class)
        @Size(min = 6, max = 30)
        @Pattern(regexp = NAME_PATTERN)
        String name,

        @NotBlank(groups = ForCreate.class)
        @Size(min = 12, max = 1000)
        @Pattern(regexp = DESCRIPTION_PATTERN)
        String description,

        @Positive
        @Digits(integer = 3, fraction = 2)
        BigDecimal price,

        @Positive(groups = ForCreate.class)
        @Digits(integer = 2, fraction = 0)
        Integer duration,

        @NotNull(groups = ForCreate.class)
        @FileImage
        MultipartFile image,

        @NotBlank(groups = ForCreate.class)
        @Size(max = 15)
        String category,

        Set<@NotBlank @Size(min = 3, max = 15) @Pattern(regexp = TAG_PATTERN) String> tags
) {
        private static final String NAME_PATTERN = "^[\\p{Alpha}А-ЯЁа-яё][\\p{Alpha}А-ЯЁа-яё\\d\\s_]{5,29}$";
        private static final String DESCRIPTION_PATTERN =
                "^[\\p{Alpha}А-ЯЁа-яё][\\p{Alpha}А-ЯЁа-яё\\d\\s,;.:?!\"'%()]{11,999}$";
        private static final String TAG_PATTERN = "^[\\p{Alnum}]{3,15}$|^[А-ЯЁа-яё\\d]{3,15}$";
}
