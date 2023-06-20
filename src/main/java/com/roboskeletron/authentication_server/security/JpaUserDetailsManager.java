package com.roboskeletron.authentication_server.security;

import com.roboskeletron.authentication_server.service.UserScopeService;
import com.roboskeletron.authentication_server.service.UserService;
import com.roboskeletron.authentication_server.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserService userService;
    private final UserScopeService userScopeService;

    @Override
    public void createUser(UserDetails user) {
        userService.createUser(UserMapper.fromUserDetails(user, userScopeService));
    }

    @Override
    public void updateUser(UserDetails user) {
        userService.updateUser(UserMapper.fromUserDetails(user, userScopeService));
    }

    @Override
    public void deleteUser(String username) {
        userService.deleteUser(userService.getUser(username));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userService.doesUserExists(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUser(username);
    }
}
