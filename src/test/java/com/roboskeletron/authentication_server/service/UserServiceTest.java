package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    private UserService service;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();

        service = new UserService(userRepository);
    }

    @Test
    void createUser() {
        String username = "User";
        User user = User.builder()
                .username(username)
                .password("password")
                .build();

        service.createUser(user);

        User actualUser = service.getUser(username);

        assertThat(actualUser.getUsername()).isEqualTo(username);
    }
}