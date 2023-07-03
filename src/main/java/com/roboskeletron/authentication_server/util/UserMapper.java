package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class UserMapper {
    private static final Function<String, UserAuthority> defaultAuthorityFunc = authority -> UserAuthority
            .builder().name(authority).build();

    private static Function<String, UserAuthority> authorityFunc = defaultAuthorityFunc;

    public static User fromUserDetails(UserDetails userDetails){
        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .userAuthorities(SetMapper.mapObjectToSet(GrantedAuthority::getAuthority, authorityFunc,
                        new HashSet<>(userDetails.getAuthorities())))
                .build();
    }

    public static Set<String> getAuthoritiesAsSet(User user){
        var authorities = user.getUserAuthorities();

        return SetMapper.mapObjectToSet(UserAuthority::getAuthority, String::toString,
                authorities);
    }

    public static Function<String, UserAuthority> getAuthorityFunc() {
        return authorityFunc;
    }

    public static void setAuthorityFunc(Function<String, UserAuthority> authorityFunc) {
        UserMapper.authorityFunc = authorityFunc;
    }

    public static Function<String, UserAuthority> getDefaultAuthorityFunc(){
        return defaultAuthorityFunc;
    }
}
