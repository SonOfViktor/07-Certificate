package com.epam.esm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record TagDto(int id,
                     @NotBlank
                     @Size(min = 2, max = 45)
                     String name) {
}
