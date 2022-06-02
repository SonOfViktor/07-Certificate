package com.epam.esm.dto;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public record CertificateTagsDto(
        @NotNull
        @Valid
        GiftCertificate certificate,

        @Valid
        List<Tag> tags) {

    @Override
    public String toString() {
        return certificate + "\n" + tags;
    }
}
