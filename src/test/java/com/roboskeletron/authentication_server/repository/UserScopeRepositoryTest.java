package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.UserScope;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserScopeRepositoryTest {
    @Autowired
    UserScopeRepository repository;

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
}