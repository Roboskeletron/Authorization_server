package com.roboskeletron.authentication_server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "client_id", unique = true)
    private String clientId;

    @Column(name = "secret")
    private String clientSecret;

    @Column(name = "redirect_uri")
    private String redirectUri;

    @ManyToMany
    private Set<ClientScope> scopes;

    @ManyToMany
    private Set<AuthenticationMethod> authenticationMethods;

    @ManyToMany
    private Set<AuthorizationGrantType> authorizationGrantTypes;
}
