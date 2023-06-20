package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.AuthorizationGrantType;
import com.roboskeletron.authentication_server.repository.AuthorizationGrantTypeRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationGrantTypeService {
    private final AuthorizationGrantTypeRepository authorizationGrantTypeRepository;

    public void createGrantType(AuthorizationGrantType grantType){
        if (authorizationGrantTypeRepository.existsByName(grantType.getName()))
            throw new EntityExistsException("name " + grantType.getName() + " has been taken");

        authorizationGrantTypeRepository.save(grantType);
    }

    public AuthorizationGrantType updateGrantType(AuthorizationGrantType grantType){
        if (!authorizationGrantTypeRepository.existsById(grantType.getId()))
            throw new EntityNotFoundException("grantType not found");

        return authorizationGrantTypeRepository.save(grantType);
    }

    public void deleteGrantType(AuthorizationGrantType grantType){
        if (!authorizationGrantTypeRepository.existsById(grantType.getId()))
            throw new EntityNotFoundException("grantType not found");

        authorizationGrantTypeRepository.delete(grantType);
    }

    public void deleteGrantType(int id){
        if (!authorizationGrantTypeRepository.existsById(id))
            throw new EntityNotFoundException("grantType not found");

        authorizationGrantTypeRepository.deleteById(id);
    }

    public AuthorizationGrantType getGrantType(int id){
        return authorizationGrantTypeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public AuthorizationGrantType getGrantType(String username){
        return authorizationGrantTypeRepository.findByName(username).orElseThrow(EntityNotFoundException::new);
    }
}
