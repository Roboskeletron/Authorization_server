package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.service.UserScopeService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

public class UserMapper {
    public static User fromUserDetails(UserDetails userDetails, UserScopeService service){
        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .scopes(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .map(service::getUserScope).collect(Collectors.toSet()))
                .build();
    }
}
