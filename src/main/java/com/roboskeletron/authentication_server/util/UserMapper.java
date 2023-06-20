package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserScope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.function.Function;

public class UserMapper {
    private static final Function<String, UserScope> defaultScopeFunc = scope -> UserScope
            .builder().name(scope).build();

    private static Function<String, UserScope> scopeFunc = defaultScopeFunc;

    public static User fromUserDetails(UserDetails userDetails){
        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .scopes(SetMapper.mapObjectToSet(GrantedAuthority::getAuthority, scopeFunc,
                        new HashSet<>(userDetails.getAuthorities())))
                .build();
    }

    public static Function<String, UserScope> getScopeFunc() {
        return scopeFunc;
    }

    public static void setScopeFunc(Function<String, UserScope> scopeFunc) {
        UserMapper.scopeFunc = scopeFunc;
    }

    public static Function<String, UserScope> getDefaultScopeFunc(){
        return defaultScopeFunc;
    }
}
