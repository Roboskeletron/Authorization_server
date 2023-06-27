package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserScope;
import com.roboskeletron.authentication_server.repository.UserRepository;
import com.roboskeletron.authentication_server.repository.UserScopeRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user){
        if (userRepository.existsByUsername(user.getUsername()))
            throw new EntityExistsException("name " + user.getUsername() + " has been taken");

        return userRepository.save(user);
    }

    public User updateUser(User user){
        if (!userRepository.existsById(user.getId()))
            throw new EntityNotFoundException("user not found");

        return userRepository.save(user);
    }

    public void deleteUser(User user){
        if (!userRepository.existsById(user.getId()))
            throw new EntityNotFoundException("user not found");

        userRepository.delete(user);
    }

    public void deleteUser(int id){
        if (!userRepository.existsById(id))
            throw new EntityNotFoundException("user not found");

        userRepository.deleteById(id);
    }

    public User getUser(int id){
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public boolean doesUserExists(String username){
        return  userRepository.existsByUsername(username);
    }

    public boolean doesUserExists(int id){
        return userRepository.existsById(id);
    }

    public User grantScope(User user, UserScope scope){
        scope.setUser(user);
        user.getScopes().add(scope);
        return updateUser(user);
    }
}
