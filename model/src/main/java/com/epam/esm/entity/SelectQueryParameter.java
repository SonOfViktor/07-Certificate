package com.epam.esm.entity;

import javax.validation.constraints.Size;

public record SelectQueryParameter(
        @Size(max = 45)
        String tagName,

        @Size(max = 45)
        String certificateName,

        @Size(max = 45)
        String certificateDescription,

        SelectQueryOrder orderName,
        SelectQueryOrder orderDate) {
}


