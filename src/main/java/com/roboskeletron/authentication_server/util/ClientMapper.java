package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.AuthenticationMethod;
import com.roboskeletron.authentication_server.domain.Client;
import com.roboskeletron.authentication_server.domain.ClientScope;
import com.roboskeletron.authentication_server.service.AuthenticationMethodService;
import com.roboskeletron.authentication_server.service.AuthorizationGrantTypeService;
import com.roboskeletron.authentication_server.service.ClientScopeService;
import com.roboskeletron.authentication_server.service.RedirectUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ClientMapper {
    private final ClientScopeService clientScopeService;
    private final AuthenticationMethodService authenticationMethodService;
    private final AuthorizationGrantTypeService authorizationGrantTypeService;
    private final RedirectUrlService redirectUrlService;

    public static RegisteredClient mapToRegisteredClient(Client client){
        Consumer<Set<String>> scopes = strings -> strings.addAll(client.getScopes().stream()
                .map(ClientScope::getName).collect(Collectors.toSet()));

        Consumer<Set<AuthorizationGrantType>> grantTypes = authorizationGrantTypes ->
                authorizationGrantTypes.addAll(client.getAuthorizationGrantTypes().
                        stream().map(com.roboskeletron.authentication_server
                                .domain.AuthorizationGrantType::getName)
                        .map(AuthorizationGrantType::new).collect(Collectors.toSet()));

        Consumer<Set<ClientAuthenticationMethod>> authMethods = clientAuthenticationMethods ->
                clientAuthenticationMethods.addAll(client.getAuthenticationMethods().stream()
                        .map(AuthenticationMethod::getName).map(ClientAuthenticationMethod::new)
                        .collect(Collectors.toSet()));

        return RegisteredClient.
                withId(client.getId().toString())
                .clientSecret(client.getClientSecret())
                .clientId(client.getClientId())
                .scopes(scopes)
                .authorizationGrantTypes(grantTypes)
                .clientAuthenticationMethods(authMethods)
                .build();
    }

    public Client mapToClient(RegisteredClient registeredClient){
        return Client.builder()
                .id(Integer.parseInt(registeredClient.getId()))
                .clientId(registeredClient.getClientId())
                .clientSecret(registeredClient.getClientSecret())
                .scopes(registeredClient.getScopes().stream().map(clientScopeService::getClientScope).
                        collect(Collectors.toSet()))
                .authenticationMethods(registeredClient.getClientAuthenticationMethods()
                        .stream().map(ClientAuthenticationMethod::getValue)
                        .map(authenticationMethodService::getAuthMethod).collect(Collectors.toSet()))
                .authorizationGrantTypes(registeredClient.getAuthorizationGrantTypes().stream()
                        .map(AuthorizationGrantType::getValue).map(authorizationGrantTypeService::getGrantType)
                        .collect(Collectors.toSet()))
                .redirectUrls(registeredClient.getRedirectUris().stream().map(redirectUrlService::getRedirectUrl)
                        .collect(Collectors.toSet()))
                .build();
    }
}
