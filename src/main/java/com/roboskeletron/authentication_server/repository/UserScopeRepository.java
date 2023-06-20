package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserScopeRepository extends JpaRepository<UserScope, Integer> {
    Optional<UserScope> findByName(String name);
    boolean existsByName(String name);
    Optional<UserScope> findByNameAndUser(String name, User user);
}
