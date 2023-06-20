package com.roboskeletron.authentication_server.security;

import com.roboskeletron.authentication_server.domain.*;
import com.roboskeletron.authentication_server.service.*;
import com.roboskeletron.authentication_server.util.ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
    private final ClientService clientService;
    private final ClientScopeService clientScopeService;
    private final AuthenticationMethodService authenticationMethodService;
    private final AuthorizationGrantTypeService authorizationGrantTypeService;
    private final RedirectUrlService redirectUrlService;
    @Override
    public void save(RegisteredClient registeredClient) {
        var clientBuilder = new ClientBuilder()
                .setClientId(registeredClient.getClientId())
                .setSecret(registeredClient.getClientSecret());

        for (var authMethod : registeredClient.getClientAuthenticationMethods()){
            AuthenticationMethod method = authenticationMethodService.getAuthMethod(authMethod.getValue());
            clientBuilder = clientBuilder.setAuthenticationMethods(method);
        }

        for (var grantType : registeredClient.getAuthorizationGrantTypes()){
            AuthorizationGrantType type = authorizationGrantTypeService.getGrantType(grantType.getValue());
            clientBuilder = clientBuilder.setAuthorizationGrantTypes(type);
        }

        for (var url : registeredClient.getRedirectUris()){
            RedirectUrl redirectUrl = redirectUrlService.getRedirectUrl(url);
            clientBuilder = clientBuilder.setRedirectUrl(redirectUrl);
        }

        for (var scope : registeredClient.getScopes()){
            ClientScope clientScope = clientScopeService.getClientScope(scope);
            clientBuilder = clientBuilder.setScope(clientScope);
        }

        clientService.createClient(clientBuilder.build());
    }

    @Override
    public RegisteredClient findById(String id) {
        Client client = clientService.getClient(Integer.parseInt(id));

        ClientBuilder clientBuilder = new ClientBuilder(client);

        return  clientBuilder.toRegisteredClient();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client client = clientService.getClient(clientId);

        ClientBuilder clientBuilder = new ClientBuilder(client);

        return  clientBuilder.toRegisteredClient();
    }
}
