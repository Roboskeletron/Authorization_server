package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.AuthenticationMethod;
import com.roboskeletron.authentication_server.repository.AuthenticationMethodRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationMethodService {
    private final AuthenticationMethodRepository authenticationMethodRepository;

    public void createAuthMethod(AuthenticationMethod authMethod){
        if (authenticationMethodRepository.existsByName(authMethod.getName()))
            throw new EntityExistsException("name " + authMethod.getName() + " has been taken");

        authenticationMethodRepository.save(authMethod);
    }

    public AuthenticationMethod updateAuthMethod(AuthenticationMethod authMethod){
        if (!authenticationMethodRepository.existsById(authMethod.getId()))
            throw new EntityNotFoundException("authMethod not found");

        return authenticationMethodRepository.save(authMethod);
    }

    public void deleteAuthMethod(AuthenticationMethod authMethod){
        if (!authenticationMethodRepository.existsById(authMethod.getId()))
            throw new EntityNotFoundException("authMethod not found");

        authenticationMethodRepository.delete(authMethod);
    }

    public void deleteAuthMethod(int id){
        if (!authenticationMethodRepository.existsById(id))
            throw new EntityNotFoundException("authMethod not found");

        authenticationMethodRepository.deleteById(id);
    }

    public AuthenticationMethod getAuthMethod(int id){
        return authenticationMethodRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public AuthenticationMethod getAuthMethod(String username){
        return authenticationMethodRepository.findByName(username).orElseThrow(EntityNotFoundException::new);
    }
}
