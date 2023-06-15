package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.AuthorizationGrantType;
import com.roboskeletron.authentication_server.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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