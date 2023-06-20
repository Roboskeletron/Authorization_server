package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserScope;
import com.roboskeletron.authentication_server.service.UserScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;

@RequiredArgsConstructor
public class UserBuilder implements EntityBuilder<User> {
    private final User user;

    public UserBuilder(){
        this(new User());
    }

    public UserBuilder setId(int id){
        user.setId(id);
        return this;
    }

    public UserBuilder setUsername(String username){
        user.setUsername(username);
        return this;
    }

    public UserBuilder setPassword(String password){
        user.setPassword(password);
        return this;
    }

    public UserBuilder setScopes(UserScope... scopes){
        user.getScopes().addAll(Arrays.stream(scopes).toList());
        return this;
    }

    @Override
    public User build() {
        return user;
    }

    public UserBuilder fromUserDetails(UserDetails user, UserScopeService userScopeService){
        UserBuilder userBuilder = new UserBuilder()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword());

        for (var scope : user.getAuthorities()){
            UserScope userScope = userScopeService.getUserScope(scope.getAuthority());
            userBuilder = userBuilder.setScopes(userScope);
        }

        return userBuilder;
    }
}
