package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.AuthorizationGrantType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AuthorizationGrantTypeRepositoryTest {

    @Autowired
    AuthorizationGrantTypeRepository repository;

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void findByName() {
        String name = "User";

        AuthorizationGrantType expectedAuthGrandType = new AuthorizationGrantType();
        expectedAuthGrandType.setName(name);

        repository.save(expectedAuthGrandType);

        var actualAuthGrandType = repository.findByName(name);

        assertThat(actualAuthGrandType.isPresent()).isTrue();
        assertThat(actualAuthGrandType.get().getName()).isEqualTo(name);
    }
}