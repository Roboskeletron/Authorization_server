package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserAuthority;
import com.roboskeletron.authentication_server.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user){
        if (userRepository.existsByUsername(user.getUsername()))
            throw new EntityExistsException("name " + user.getUsername() + " has been taken");
        //TODO fix user creation: check that user authorities has link to user
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
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User getUser(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public boolean doesUserExists(String username){
        return  userRepository.existsByUsername(username);
    }

    public boolean doesUserExists(int id){
        return userRepository.existsById(id);
    }

    public User grantAuthority(User user, UserAuthority authority){
        authority.setUser(user);
        user.getUserAuthorities().add(authority);
        return updateUser(user);
    }

    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }
}
