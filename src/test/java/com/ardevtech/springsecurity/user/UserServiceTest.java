package com.ardevtech.springsecurity.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 등록")
    void signup() {
        // Given
        String username = "user123";
        String password = "password";

        // When
        User user = userService.signup(username, password);

        // Then
        then(user.getId()).isNotNull();
        then(user.getUsername()).isEqualTo("user123");
        then(user.getPassword()).startsWith("{bcrypt}"); // 패스워드가 {bcrypt} 시작하는지 검증
        then(user.getAuthorities()).hasSize(1);
        then(user.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_USER");
        then(user.isAdmin()).isFalse();
        then(user.isAccountNonExpired()).isTrue();
        then(user.isAccountNonLocked()).isTrue();
        then(user.isEnabled()).isTrue();
        then(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("어드민 등록")
    void signupAdmin() {
        // Given
        String username = "admin123";
        String password = "password";

        // When
        User user = userService.signupAdmin(username, password);

        // Then
        then(user.getId()).isNotNull();
        then(user.getUsername()).isEqualTo("admin123");
        then(user.getPassword()).startsWith("{bcrypt");
        then(user.getAuthorities()).hasSize(1);
        then(user.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_ADMIN");
        then(user.isAdmin()).isTrue();
        then(user.isAccountNonExpired()).isTrue();
        then(user.isAccountNonLocked()).isTrue();
        then(user.isEnabled()).isTrue();
        then(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("유저 검색")
    void findByUsername() {
        // Given
        userRepository.save(new User("user123", "password", "ROLE_USER"));

        // When
        User user = userService.findByUsername("user123");

        // Then
        then(user.getId()).isNotNull();
    }
}