package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.ClientScope;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ClientScopeRepositoryTest {

    @Autowired
    ClientScopeRepository repository;

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void findByName() {
        String name = "User";

        ClientScope expectedScope = new ClientScope();
        expectedScope.setName(name);

        repository.save(expectedScope);

        var actualScope = repository.findByName(name);

        assertThat(actualScope.isPresent()).isTrue();
        assertThat(actualScope.get().getName()).isEqualTo(name);
    }
}