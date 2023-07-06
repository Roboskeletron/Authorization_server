package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserAuthority;
import com.roboskeletron.authentication_server.repository.UserAuthorityRepository;
import com.roboskeletron.authentication_server.repository.UserRepository;
import com.roboskeletron.authentication_server.util.SetMapper;
import com.roboskeletron.authentication_server.util.UserMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository authorityRepository;

    public User createUser(User user){
        if (userRepository.existsByUsername(user.getUsername()))
            throw new EntityExistsException("name " + user.getUsername() + " has been taken");
        Set<UserAuthority> authorities = new HashSet<>(user.getUserAuthorities());
        user.getUserAuthorities().clear();

        User savedUser = userRepository.save(user);

        authorities.forEach(authority -> grantAuthority(savedUser, authority));

        return updateUser(savedUser);
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

    public void grantAuthority(User user, UserAuthority authority){
        authority.setUser(user);
        user.getUserAuthorities().add(authority);
    }

    public void revokeAuthority(User user, String authority){
        var authorities = user.getUserAuthorities().stream().filter(userAuthority ->
                userAuthority.getName().equals(authority)).collect(Collectors.toSet());
        user.getUserAuthorities().removeAll(authorities);
        authorityRepository.deleteAll(authorities);
    }

    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }
}
