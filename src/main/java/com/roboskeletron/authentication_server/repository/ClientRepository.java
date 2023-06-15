package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByClientId(String name);
    boolean existsByClientId(String name);
}
