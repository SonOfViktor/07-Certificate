package com.epam.esm.security;

import com.epam.esm.dto.SecurityUserDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Optional;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.security.core.userdetails.User.withUsername;

@Component
@RequiredArgsConstructor
public class JpaUserDetailService implements UserDetailsService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.findByEmail(email)
                .map(this::createSecurityUserDtoFromUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
       return (jwtTokenProvider.validateToken(jwtToken)) ?
               Optional.of(createUserDetailsFromJwtToken(jwtToken)) :
               Optional.empty();
    }

    private SecurityUserDto createSecurityUserDtoFromUser(User user) {
        return new SecurityUserDto.Builder()
                .withUserId(user.getUserId())
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withAuthorities(user.getRole().name())
                .build();
    }

    private UserDetails createUserDetailsFromJwtToken(String jwtToken) {
        return withUsername(jwtTokenProvider.getUsername(jwtToken))
                .authorities(jwtTokenProvider.getRole(jwtToken))
                .password(EMPTY)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
