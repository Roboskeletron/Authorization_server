package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserScope;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

public class UserMapper {
    public static User fromUserDetails(UserDetails userDetails){
        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .scopes(userDetails.getAuthorities().stream()
                        .map(scope -> UserScope.builder().name(scope.getAuthority()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
