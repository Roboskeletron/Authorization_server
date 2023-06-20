package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.AuthenticationMethod;
import com.roboskeletron.authentication_server.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationMethodRepository extends JpaRepository<AuthenticationMethod, Integer> {
    Optional<AuthenticationMethod> findByName(String name);
    boolean existsByName(String name);
    Optional<AuthenticationMethod> findByNameAndClient(String name, Client client);
}
