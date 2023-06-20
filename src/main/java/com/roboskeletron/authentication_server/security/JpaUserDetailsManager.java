package com.roboskeletron.authentication_server.security;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.service.UserService;
import com.roboskeletron.authentication_server.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserService userService;

    @Override
    public void createUser(UserDetails user) {
        userService.createUser(UserMapper.fromUserDetails(user));
    }

    @Override
    @Deprecated
    public void updateUser(UserDetails user) {
        userService.updateUser(UserMapper.fromUserDetails(user));
    }

    public void updateUser(User user){
        userService.updateUser(user);
    }

    public void changePassword(User user, String oldPassword, String newPassword){
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new BadCredentialsException("Invalid password");

        user.setPassword(passwordEncoder.encode(newPassword));

        updateUser(user);
    }

    @Override
    public void deleteUser(String username) {
        userService.deleteUser(userService.getUser(username));
    }

    @Override
    @Deprecated
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
