package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.Client;
import com.roboskeletron.authentication_server.repository.ClientRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public void createClient(Client client){
        if (clientRepository.existsByClientId(client.getClientId()))
            throw new EntityExistsException("name " + client.getClientId() + " has been taken");

        clientRepository.save(client);
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

    public boolean doesClientExists(String clientId) {
        return clientRepository.existsByClientId(clientId);
    }

    public boolean doesClientExists(int id){
        return clientRepository.existsById(id);
    }
}
