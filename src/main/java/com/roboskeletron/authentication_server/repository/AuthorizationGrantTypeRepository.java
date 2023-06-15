package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.AuthorizationGrantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationGrantTypeRepository extends JpaRepository<AuthorizationGrantType, Integer> {
    Optional<AuthorizationGrantType> findByName(String name);
    boolean existsByName(String name);
}
