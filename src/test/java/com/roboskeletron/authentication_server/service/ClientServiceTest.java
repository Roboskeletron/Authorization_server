package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.Client;
import com.roboskeletron.authentication_server.domain.ClientScope;
import com.roboskeletron.authentication_server.repository.ClientRepository;
import com.roboskeletron.authentication_server.repository.ClientScopeRepository;
import com.roboskeletron.authentication_server.util.ClientMapper;
import com.roboskeletron.authentication_server.util.SetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientServiceTest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientScopeRepository scopeRepository;
    private ClientService service;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        scopeRepository.deleteAll();
        service = new ClientService(clientRepository);
    }

    @Test
    void createClient() {
        String clientId = "client";
        Client client = Client.builder()
                .clientId(clientId)
                .clientSecret("secret")
                .scopes(SetMapper.mapFromStrings(ClientMapper.getDefaultScopeFunc(), "profile"))
                .authenticationMethods(SetMapper.mapFromStrings(ClientMapper.getDefaultAuthMethodFunc(), "client_secret_basic"))
                .authorizationGrantTypes(SetMapper.mapFromStrings(ClientMapper.getDefaultGrantTypeFunc(), "authorization_code",
                        "refresh_token"))
                .redirectUrls(SetMapper.mapFromStrings(ClientMapper.getDefaultRedirectUrlFunc(), "url"))
                .build();

        client = service.createClient(client);

        var actualClient = service.getClient(clientId);

        assertThat(actualClient).isEqualTo(client);
    }

    @Test
    void grantScope() {
        String clientId = "client";
        Client client = Client.builder()
                .clientId(clientId)
                .clientSecret("secret")
                .scopes(SetMapper.mapFromStrings(ClientMapper.getDefaultScopeFunc(), "profile"))
                .authenticationMethods(SetMapper.mapFromStrings(ClientMapper.getDefaultAuthMethodFunc(), "client_secret_basic"))
                .authorizationGrantTypes(SetMapper.mapFromStrings(ClientMapper.getDefaultGrantTypeFunc(), "authorization_code",
                        "refresh_token"))
                .redirectUrls(SetMapper.mapFromStrings(ClientMapper.getRedirectUrlFunc(), "url"))
                .build();

        client = service.createClient(client);

        String scopeName = "openid";
        client = service.grantScope(client, ClientScope.builder().name(scopeName).build());

        assertThat(client.getScopes().stream().map(ClientScope::getName).collect(Collectors.toSet())
                .contains(scopeName)).isTrue();
    }
}