package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Arrays;

@RequiredArgsConstructor
public class ClientBuilder implements EntityBuilder<Client> {
    private final Client client;

    public ClientBuilder(){
        this(new Client());
    }

    public ClientBuilder setId(int id){
        client.setId(id);
        return this;
    }

    public ClientBuilder setClientId(String clientId){
        client.setClientId(clientId);
        return this;
    }

    public ClientBuilder setSecret(String secret){
        client.setClientSecret(secret);
        return this;
    }

    public ClientBuilder setRedirectUrl(RedirectUrl... urls){
        client.getRedirectUrls().addAll(Arrays.stream(urls).toList());
        return this;
    }

    public ClientBuilder setAuthenticationMethods(AuthenticationMethod... authenticationMethods){
        client.getAuthenticationMethods().addAll(Arrays.stream(authenticationMethods).toList());
        return this;
    }

    public ClientBuilder setAuthorizationGrantTypes(AuthorizationGrantType... authorizationGrantTypes) {
        client.getAuthorizationGrantTypes().addAll(Arrays.stream(authorizationGrantTypes).toList());
        return this;
    }

    @Override
    public Client build() {
        return client;
    }

    public ClientBuilder setScope(ClientScope... clientScopes) {
        client.getScopes().addAll(Arrays.stream(clientScopes).toList());
        return  this;
    }

    public RegisteredClient toRegisteredClient(){
        RegisteredClient.Builder clientBuilder = RegisteredClient.withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret());

        for (var scope : client.getScopes()){
            clientBuilder = clientBuilder.scope(scope.getName());
        }

        for (var url : client.getRedirectUrls()){
            clientBuilder = clientBuilder.redirectUri(url.getUrl());
        }

        for (var authMethod : client.getAuthenticationMethods()){
            clientBuilder = clientBuilder.clientAuthenticationMethod(new ClientAuthenticationMethod(authMethod.getName()));
        }

        for (var grantType : client.getAuthorizationGrantTypes()){
            clientBuilder = clientBuilder.authorizationGrantType(
                    new org.springframework.security.oauth2.core.AuthorizationGrantType(
                            grantType.getName()
                    ));
        }

        return clientBuilder.build();
    }
}
