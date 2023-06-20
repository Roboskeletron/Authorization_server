package com.roboskeletron.authentication_server.security;

import com.roboskeletron.authentication_server.domain.Client;
import com.roboskeletron.authentication_server.service.*;
import com.roboskeletron.authentication_server.util.ClientMapper;
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
        ClientMapper mapper = new ClientMapper(
                clientScopeService,
                authenticationMethodService,
                authorizationGrantTypeService,
                redirectUrlService
        );

        clientService.createClient(mapper.mapToClient(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        Client client = clientService.getClient(Integer.parseInt(id));

        return ClientMapper.mapToRegisteredClient(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client client = clientService.getClient(clientId);

        return  ClientMapper.mapToRegisteredClient(client);
    }
}
