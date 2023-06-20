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
    @Override
    public void save(RegisteredClient registeredClient) {
        clientService.createClient(ClientMapper.mapToClient(registeredClient));
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
