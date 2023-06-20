package com.roboskeletron.authentication_server.security;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.service.UserScopeService;
import com.roboskeletron.authentication_server.service.UserService;
import com.roboskeletron.authentication_server.util.UserBuilder;
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
        UserBuilder userBuilder = new UserBuilder().fromUserDetails(user, userScopeService);

        userService.createUser(userBuilder.build());
    }

    @Override
    public void updateUser(UserDetails user) {
        User foundUser = userService.getUser(user.getUsername());

        UserBuilder userBuilder = new UserBuilder(foundUser).fromUserDetails(user, userScopeService);

        userService.updateUser(userBuilder.build());
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
