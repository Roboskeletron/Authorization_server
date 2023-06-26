package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserMapper {
    public static User fromUserDetails(UserDetails userDetails) {
        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .build();
    }
}
