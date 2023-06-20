package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository repository;

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void findByName() {
        String name = "User";

        User expextedUser = new User();
        expextedUser.setUsername(name);
        expextedUser.setPassword("random");
        expextedUser.setUserAuthorities(Collections.emptySet());

        repository.save(expextedUser);

        var actualUser = repository.findByUsername(name);

        assertThat(actualUser.isPresent()).isTrue();
        assertThat(actualUser.get().getUsername()).isEqualTo(name);
    }
}