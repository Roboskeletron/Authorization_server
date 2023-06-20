package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.AuthenticationMethod;
import com.roboskeletron.authentication_server.domain.Client;
import com.roboskeletron.authentication_server.domain.ClientScope;
import com.roboskeletron.authentication_server.domain.RedirectUrl;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.function.Function;

public class ClientMapper {
    private static final Function<String, ClientScope> defaultScopeFunc = scope -> ClientScope.builder().name(scope).build();

    private static final Function<String, AuthenticationMethod> defaultAuthMethodFunc = method ->
            AuthenticationMethod.builder().name(method).build();

    private static final Function<String, com.roboskeletron.authentication_server.domain.AuthorizationGrantType> defaultGrantTypeFunc =
            method -> com.roboskeletron.authentication_server.domain.AuthorizationGrantType
                    .builder().name(method).build();

    private static final Function<String, RedirectUrl> defaultRedirectUrlFunc = url -> RedirectUrl.builder().url(url).build();

    private static Function<String, ClientScope> scopeFunc = defaultScopeFunc;
    private static Function<String, AuthenticationMethod> authMethodFunc = defaultAuthMethodFunc;
    private static Function<String, com.roboskeletron.authentication_server.domain.AuthorizationGrantType> grantTypeFunc = defaultGrantTypeFunc;
    private static Function<String, RedirectUrl> redirectUrlFunc =defaultRedirectUrlFunc;

    public static RegisteredClient mapToRegisteredClient(Client client){
        SetMapper.ActionType actionType = SetMapper.ActionType.ADD;

        var scopeConsumer = SetMapper.getSetStringConsumer(actionType, ClientScope::getName, String::toString, client.getScopes());

        var grantTypesConsumer = SetMapper.getSetStringConsumer(actionType, com.roboskeletron.authentication_server.domain
                .AuthorizationGrantType::getName, AuthorizationGrantType::new, client.getAuthorizationGrantTypes());

        var authMethodsConsumer = SetMapper.getSetStringConsumer(actionType, AuthenticationMethod::getName, ClientAuthenticationMethod::new,
                client.getAuthenticationMethods());

       var redirectUrlsConsumer = SetMapper.getSetStringConsumer(actionType, RedirectUrl::getUrl, String::toString, client.getRedirectUrls());

        return RegisteredClient.
                withId(client.getId().toString())
                .clientSecret(client.getClientSecret())
                .clientId(client.getClientId())
                .scopes(scopeConsumer)
                .authorizationGrantTypes(grantTypesConsumer)
                .clientAuthenticationMethods(authMethodsConsumer)
                .redirectUris(redirectUrlsConsumer)
                .build();
    }

    public static Client mapToClient(RegisteredClient registeredClient){
        var client = Client.builder()
                .id(Integer.parseInt(registeredClient.getId()))
                .clientId(registeredClient.getClientId())
                .clientSecret(registeredClient.getClientSecret())
                .build();

        var scopes = SetMapper.mapObjectToSet(String::toString, scopeFunc, registeredClient.getScopes());
        scopes.forEach(clientScope -> clientScope.setClient(client));

        var authMethods = SetMapper.mapObjectToSet(ClientAuthenticationMethod::getValue, authMethodFunc,
                registeredClient.getClientAuthenticationMethods());
        authMethods.forEach(authenticationMethod -> authenticationMethod.setClient(client));

        var authGrantTypes = SetMapper.mapObjectToSet(AuthorizationGrantType::getValue, grantTypeFunc,
                registeredClient.getAuthorizationGrantTypes());
        authGrantTypes.forEach(authorizationGrantType -> authorizationGrantType.setClient(client));

        var redirectUrls = SetMapper.mapObjectToSet(String::toString, redirectUrlFunc, registeredClient.getRedirectUris());
        redirectUrls.forEach(redirectUrl -> redirectUrl.setClient(client));

        return  client.toBuilder()
                .scopes(scopes)
                .authenticationMethods(authMethods)
                .authorizationGrantTypes(authGrantTypes)
                .redirectUrls(redirectUrls)
                .build();
    }

    public static Function<String, ClientScope> getDefaultScopeFunc() {
        return defaultScopeFunc;
    }

    public static Function<String, AuthenticationMethod> getDefaultAuthMethodFunc() {
        return defaultAuthMethodFunc;
    }

    public static Function<String, com.roboskeletron.authentication_server.domain.AuthorizationGrantType> getDefaultGrantTypeFunc() {
        return defaultGrantTypeFunc;
    }

    public static Function<String, RedirectUrl> getDefaultRedirectUrlFunc() {
        return defaultRedirectUrlFunc;
    }

    public static Function<String, ClientScope> getScopeFunc() {
        return scopeFunc;
    }

    public static void setScopeFunc(Function<String, ClientScope> scopeFunc) {
        ClientMapper.scopeFunc = scopeFunc;
    }

    public static Function<String, AuthenticationMethod> getAuthMethodFunc() {
        return authMethodFunc;
    }

    public static void setAuthMethodFunc(Function<String, AuthenticationMethod> authMethodFunc) {
        ClientMapper.authMethodFunc = authMethodFunc;
    }

    public static Function<String, com.roboskeletron.authentication_server.domain.AuthorizationGrantType> getGrantTypeFunc() {
        return grantTypeFunc;
    }

    public static void setGrantTypeFunc(Function<String, com.roboskeletron.authentication_server.domain.AuthorizationGrantType> grantTypeFunc) {
        ClientMapper.grantTypeFunc = grantTypeFunc;
    }

    public static Function<String, RedirectUrl> getRedirectUrlFunc() {
        return redirectUrlFunc;
    }

    public static void setRedirectUrlFunc(Function<String, RedirectUrl> redirectUrlFunc) {
        ClientMapper.redirectUrlFunc = redirectUrlFunc;
    }
}
