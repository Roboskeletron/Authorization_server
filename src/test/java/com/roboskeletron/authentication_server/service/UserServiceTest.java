package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserAuthority;
import com.roboskeletron.authentication_server.repository.UserRepository;
import com.roboskeletron.authentication_server.repository.UserAuthorityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthorityRepository scopeRepository;
    private UserService service;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        scopeRepository.deleteAll();

        service = new UserService(userRepository, scopeRepository);
    }

    @Test
    void createUser() {
        Set<UserAuthority> scopes = new HashSet<>();
        scopes.add(UserAuthority.builder().name("Admin").build());
        scopes.add(UserAuthority.builder().name("User").build());

        String username = "User";
        User user = User.builder()
                .username(username)
                .password("password")
                .userAuthorities(scopes)
                .build();

        service.createUser(user);

        User actualUser = service.getUser(username);
        var actualScopes = actualUser.getUserAuthorities().stream().map(UserAuthority::getName)
                .collect(Collectors.toSet());

        assertThat(actualUser.getUsername()).isEqualTo(username);
        assertThat(actualScopes).isEqualTo(scopes.stream().map(UserAuthority::getName)
                .collect(Collectors.toSet()));
    }

    @Test
    void grantAuthority() {
        String username = "User";
        User user = User.builder()
                .username(username)
                .password("password")
                .userAuthorities(new HashSet<>())
                .build();

        user = service.createUser(user);

        String scope = "User";
        service.grantAuthority(user, UserAuthority.builder().name(scope).build());

        user = service.updateUser(user);

        assertThat(user.getUserAuthorities().stream().map(UserAuthority::getName).collect(Collectors.toSet())
                .contains(scope)).isTrue();
    }
}