package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserScope;
import com.roboskeletron.authentication_server.repository.UserRepository;
import com.roboskeletron.authentication_server.repository.UserScopeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserScopeRepository scopeRepository;
    private UserService service;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        scopeRepository.deleteAll();

        service = new UserService(userRepository);
    }

    @Test
    void createUser() {
        Set<UserScope> scopes = new HashSet<>();
        scopes.add(UserScope.builder().name("Admin").build());
        scopes.add(UserScope.builder().name("User").build());

        String username = "User";
        User user = User.builder()
                .username(username)
                .password("password")
                .scopes(scopes)
                .build();

        service.createUser(user);

        User actualUser = service.getUser(username);
        var actualScopes = actualUser.getScopes().stream().map(UserScope::getName)
                .collect(Collectors.toSet());

        assertThat(actualUser.getUsername()).isEqualTo(username);
        assertThat(actualScopes).isEqualTo(scopes.stream().map(UserScope::getName)
                .collect(Collectors.toSet()));
    }

    @Test
    void grantScope() {
        String username = "User";
        User user = User.builder()
                .username(username)
                .password("password")
                .scopes(new HashSet<>())
                .build();

        user = service.createUser(user);

        String scope = "User";
        user = service.grantScope(user, UserScope.builder().name(scope).build());

        assertThat(user.getScopes().stream().map(UserScope::getName).collect(Collectors.toSet())
                .contains(scope)).isTrue();
    }
}