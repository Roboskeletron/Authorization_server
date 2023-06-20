package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Integer> {
    Optional<UserAuthority> findByName(String name);
    boolean existsByName(String name);
    Optional<UserAuthority> findByNameAndUser(String name, User user);
}
