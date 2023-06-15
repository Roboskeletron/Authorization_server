package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.ClientScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientScopeRepository extends JpaRepository<ClientScope, Integer> {
    Optional<ClientScope> findByName(String name);
}
