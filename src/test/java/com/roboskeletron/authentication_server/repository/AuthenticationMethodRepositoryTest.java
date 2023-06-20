package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.AuthenticationMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AuthenticationMethodRepositoryTest {
    @Autowired
    AuthenticationMethodRepository repository;

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void findByName() {
        String name = "User";

        AuthenticationMethod expectedAuthMethod = new AuthenticationMethod();
        expectedAuthMethod.setName(name);

        repository.save(expectedAuthMethod);

        var actualAuthMethod = repository.findByName(name);

        assertThat(actualAuthMethod.isPresent()).isTrue();
        assertThat(actualAuthMethod.get().getName()).isEqualTo(name);
    }
}