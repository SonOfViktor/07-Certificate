package com.epam.esm.dto;

import lombok.ToString;
import javax.validation.constraints.*;
import static org.apache.commons.lang3.StringUtils.*;

public record UserWriteDto(
        @NotNull
        @Email
        @Size(max = 30)
        String email,

        @ToString.Exclude
        @Pattern(regexp = PASSWORD_PATTERN)
        @NotNull
        String password,

        @NotNull
        @Pattern(regexp = NAME_PATTERN)
        String firstName,

        @NotNull
        @Pattern(regexp = NAME_PATTERN)
        String lastName) {

        private static final String PASSWORD_PATTERN = "(?=.*\\d)(?=.*\\p{Lower})(?=.*\\p{Upper})[\\p{Alnum}]{4,30}";
        private static final String NAME_PATTERN = "\\p{Alpha}{2,20}|[А-ЯЁа-яё]{2,20}";

        public UserWriteDto(String email, String password, String firstName, String lastName) {
                this.email = email.toLowerCase();
                this.password = password;
                this.firstName = capitalize(lowerCase(firstName));
                this.lastName = capitalize(lowerCase(lastName));
        }
}