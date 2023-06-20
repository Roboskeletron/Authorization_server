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

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<RedirectUrl> redirectUrls;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientScope> scopes;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<AuthenticationMethod> authenticationMethods;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<AuthorizationGrantType> authorizationGrantTypes;
}
