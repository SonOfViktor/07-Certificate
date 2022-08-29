package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.security.core.userdetails.User.withUsername;

@ExtendWith(MockitoExtension.class)
class JpaUserDetailServiceTest {

    @InjectMocks
    private JpaUserDetailService jpaUserDetailService;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testLoadUserByUsername() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("abra-codabra")
                .role(UserRole.USER)
                .build();
        UserDetails expected = withUsername("test@gmail.com")
                .password("abra-codabra")
                .authorities("ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

        when(userService.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        UserDetails actual = jpaUserDetailService.loadUserByUsername("test@gmail.com");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testLoadUserByNotExistedUsername() {
        when(userService.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jpaUserDetailService.loadUserByUsername("test@gmail.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with email test@gmail.com not found");
    }

    @Test
    void testLoadUserByJwtToken() {
        when(jwtTokenProvider.validateToken("token")).thenReturn(true);
        when(jwtTokenProvider.getUsername("token")).thenReturn("test@gmail.com");
        when(jwtTokenProvider.getRole("token")).thenReturn("ROLE_USER");

        UserDetails expected = withUsername("test@gmail.com")
                .authorities("ROLE_USER")
                .password("")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
        UserDetails actual = jpaUserDetailService.loadUserByJwtToken("token").get();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testLoadUserByNotValidJwtToken() {
        when(jwtTokenProvider.validateToken("token")).thenReturn(false);

        Optional<UserDetails> expected = Optional.empty();
        Optional<UserDetails> actual = jpaUserDetailService.loadUserByJwtToken("token");

        assertThat(actual).isEqualTo(expected);
    }
}