package com.roboskeletron.authentication_server.security;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.repository.UserRepository;
import com.roboskeletron.authentication_server.repository.UserScopeRepository;
import com.roboskeletron.authentication_server.service.UserService;
import com.roboskeletron.authentication_server.util.SetMapper;
import com.roboskeletron.authentication_server.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;

@DataJpaTest
class JpaUserDetailsManagerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserScopeRepository userScopeRepository;

    private JpaUserDetailsManager userDetailsManager;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        userScopeRepository.deleteAll();

        userDetailsManager = new JpaUserDetailsManager(new UserService(userRepository));
    }

    @Test
    void createUser() {
        String username = "User";

        UserDetails user = User.builder()
                .username(username)
                .password("password")
                .scopes(SetMapper.mapFromStrings(UserMapper.getDefaultScopeFunc(), "admin"))
                .build();

        userDetailsManager.createUser(user);
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void userExists() {
    }

    @Test
    void loadUserByUsername() {
    }
}