package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.Client;
import com.roboskeletron.authentication_server.domain.RedirectUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedirectUrlRepository extends JpaRepository<RedirectUrl, Integer> {
    Optional<RedirectUrl> findByUrl(String name);
    boolean existsByUrl(String name);
    Optional<RedirectUrl> findByUrlAndClient(String url, Client client);
}
