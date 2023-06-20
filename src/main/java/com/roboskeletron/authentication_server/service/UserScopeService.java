package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.UserScope;
import com.roboskeletron.authentication_server.repository.UserScopeRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserScopeService {
    private final UserScopeRepository userScopeRepository;

    public void createUserScope(UserScope scope){
        if (userScopeRepository.existsByName(scope.getName()))
            throw new EntityExistsException("name " + scope.getName() + " has been taken");

        userScopeRepository.save(scope);
    }

    public UserScope updateUserScope(UserScope scope){
        if (!userScopeRepository.existsById(scope.getId()))
            throw new EntityNotFoundException("scope not found");

        return userScopeRepository.save(scope);
    }

    public void deleteUserScope(UserScope scope){
        if (!userScopeRepository.existsById(scope.getId()))
            throw new EntityNotFoundException("scope not found");

        userScopeRepository.delete(scope);
    }

    public void deleteUserScope(int id){
        if (!userScopeRepository.existsById(id))
            throw new EntityNotFoundException("scope not found");

        userScopeRepository.deleteById(id);
    }

    public UserScope getUserScope(int id){
        return userScopeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public UserScope getUserScope(String username){
        return userScopeRepository.findByName(username).orElseThrow(EntityNotFoundException::new);
    }
}
