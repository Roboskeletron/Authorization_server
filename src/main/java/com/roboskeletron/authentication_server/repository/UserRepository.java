package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String name);
    boolean existsByUsername(String name);
}
