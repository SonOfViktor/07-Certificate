package com.epam.esm.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import java.util.Collections;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SecurityUserDto extends User {
    public static final String ROLE = "ROLE_";
    private final int userId;
    private final String firstName;
    private final String lastName;

    private SecurityUserDto(Builder builder) {
        super(builder.email, builder.password, builder.authorities);
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    public static class Builder {
        private int userId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;

        public Builder withUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withAuthorities(String role) {
            this.authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE + role));
            return this;
        }

        public SecurityUserDto build() {
            return new SecurityUserDto(this);
        }
    }
}