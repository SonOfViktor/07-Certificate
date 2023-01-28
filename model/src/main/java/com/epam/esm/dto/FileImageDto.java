package com.epam.esm.dto;

import java.io.InputStream;

public record FileImageDto(
        InputStream fileContent,
        String fileExtension
) {
}