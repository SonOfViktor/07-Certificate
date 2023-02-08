package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
public class UserReadDto {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    @ToString.Exclude
    private String token;
}