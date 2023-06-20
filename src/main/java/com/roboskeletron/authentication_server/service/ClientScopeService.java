package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.ClientScope;
import com.roboskeletron.authentication_server.repository.ClientScopeRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientScopeService {
    private final ClientScopeRepository clientScopeRepository;

    public void createClientScope(ClientScope scope){
        if (clientScopeRepository.existsByName(scope.getName()))
            throw new EntityExistsException("name " + scope.getName() + " has been taken");

        clientScopeRepository.save(scope);
    }

    public ClientScope updateClientScope(ClientScope scope){
        if (!clientScopeRepository.existsById(scope.getId()))
            throw new EntityNotFoundException("scope not found");

        return clientScopeRepository.save(scope);
    }

    public void deleteClientScope(ClientScope scope){
        if (!clientScopeRepository.existsById(scope.getId()))
            throw new EntityNotFoundException("scope not found");

        clientScopeRepository.delete(scope);
    }

    public void deleteClientScope(int id){
        if (!clientScopeRepository.existsById(id))
            throw new EntityNotFoundException("scope not found");

        clientScopeRepository.deleteById(id);
    }

    public ClientScope getClientScope(int id){
        return clientScopeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public ClientScope getClientScope(String username){
        return clientScopeRepository.findByName(username).orElseThrow(EntityNotFoundException::new);
    }
}
