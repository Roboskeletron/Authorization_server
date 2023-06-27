package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserScope;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserScopeRepositoryTest {
    @Autowired
    UserScopeRepository repository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void findByName() {
        String name = "scope";

        UserScope expectedScope = new UserScope();
        expectedScope.setName(name);

        repository.save(expectedScope);

        var actualScope = repository.findByName(name);

        assertThat(actualScope.isPresent()).isTrue();
        assertThat(actualScope.get().getName()).isEqualTo(name);
    }

    @Test
    void findByNameAndUser() {
        User user = User.builder()
                .username("user")
                .password("password")
                .scopes(Collections.emptySet())
                .build();

        user = userRepository.save(user);

        String name = "user";
        UserScope scope = UserScope.builder()
                .name(name)
                .user(user)
                .build();

        scope = repository.save(scope);

        var actualScope = repository.findByNameAndUser(name, user);

        assertThat(actualScope.isPresent()).isTrue();
        assertThat(actualScope.get()).isEqualTo(scope);
    }
}