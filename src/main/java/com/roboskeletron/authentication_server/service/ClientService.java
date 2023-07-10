package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.*;
import com.roboskeletron.authentication_server.exception.ClientGetPropertyException;
import com.roboskeletron.authentication_server.repository.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientScopeRepository scopeRepository;
    private final AuthenticationMethodRepository  authenticationMethodRepository;
    private final AuthorizationGrantTypeRepository authorizationGrantTypeRepository;
    private final RedirectUrlRepository redirectUrlRepository;

    public Client createClient(Client client){
        if (clientRepository.existsByClientId(client.getClientId()))
            throw new EntityExistsException("name " + client.getClientId() + " has been taken");

        Set<ClientScope> scopes = new HashSet<>(client.getScopes());
        Set<AuthenticationMethod> authenticationMethods = new HashSet<>(client
                .getAuthenticationMethods());
        Set<AuthorizationGrantType> authorizationGrantTypes = new HashSet<>(client
                .getAuthorizationGrantTypes());
        Set<RedirectUrl> redirectUrls = new HashSet<>(client.getRedirectUrls());

        scopes.forEach(scope -> grantProperty(client, scope));
        authenticationMethods.forEach(method -> grantProperty(client, method));
        authorizationGrantTypes.forEach(grantType -> grantProperty(client, grantType));
        redirectUrls.forEach(redirectUrl -> grantProperty(client, redirectUrl));

        return clientRepository.save(client);
    }

    public Client updateClient(Client client){
        if (!clientRepository.existsById(client.getId()))
            throw new EntityNotFoundException("client not found");

        return clientRepository.save(client);
    }

    public void deleteClient(Client client){
        if (!clientRepository.existsById(client.getId()))
            throw new EntityNotFoundException("client not found");

        clientRepository.delete(client);
    }

    public void deleteClient(int id){
        if (!clientRepository.existsById(id))
            throw new EntityNotFoundException("client not found");

        clientRepository.deleteById(id);
    }

    public Client getClient(int id){
        return clientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Client getClient(String clientId){
        return clientRepository.findByClientId(clientId).orElseThrow(EntityNotFoundException::new);
    }

    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public boolean doesClientExists(String clientId) {
        return clientRepository.existsByClientId(clientId);
    }

    public boolean doesClientExists(int id){
        return clientRepository.existsById(id);
    }

    public  <T extends ClientProperty> void grantProperty(Client client, T property){
        Set<T> propertySet = getTSet(client, property);
        property.setClient(client);
        propertySet.add(property);
    }

    public <T extends ClientProperty> void revokeProperty(Client client, T property){
        Set<T> propertySet = getTSet(client, property);
        var repository = getRepository(property);
        Set<T> properties = propertySet.stream().filter(p -> p.equals(property)).collect(Collectors.toSet());
        propertySet.removeAll(properties);
        repository.deleteAll(properties);
    }

    private <T extends ClientProperty> Set<T> getTSet(Client client, T type){
        Optional<Method> propertySetMethod = Arrays.stream(Client.class.getMethods())
                .filter(method -> method.getGenericReturnType().getTypeName().contains(
                        type.getClass().getName())).findFirst();

        if (propertySetMethod.isEmpty())
            throw new ClientGetPropertyException("Method not found", null);

        try {
            return  (Set<T>) propertySetMethod.get().invoke(client);
        }
        catch (InvocationTargetException | IllegalAccessException exception){
            throw new ClientGetPropertyException("Unable to invoke property method "
                    + propertySetMethod.get().getName(),
                    exception.getMessage());
        }
    }

    private <T extends ClientProperty> JpaRepository getRepository(T property){
        String className = property.getClass().getName();

        if (className.equals(ClientScope.class.getName()))
            return scopeRepository;

        if (className.equals(AuthenticationMethod.class.getName()))
            return authenticationMethodRepository;

        if (className.equals(AuthorizationGrantType.class.getName()))
            return authorizationGrantTypeRepository;

        if (className.equals(RedirectUrl.class.getName()))
            return redirectUrlRepository;

        throw new ClientGetPropertyException("Unable to get repository for property " + className, null);
    }
}
