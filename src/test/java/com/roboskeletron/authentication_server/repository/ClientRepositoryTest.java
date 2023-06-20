package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    ClientRepository repository;

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void findByName() {
        String name = "User";

        Client expectedClient = new Client();
        expectedClient.setClientId(name);
        expectedClient.setClientSecret("");
        expectedClient.setAuthenticationMethods(Collections.emptySet());
        expectedClient.setAuthorizationGrantTypes(Collections.emptySet());
        expectedClient.setRedirectUrls(Collections.emptySet());
        expectedClient.setScopes(Collections.emptySet());

        repository.save(expectedClient);

        var actualClient = repository.findByClientId(name);

        assertThat(actualClient.isPresent()).isTrue();
        assertThat(actualClient.get().getClientId()).isEqualTo(name);
    }
}