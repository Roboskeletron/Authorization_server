package com.roboskeletron.authentication_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "clients")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "client_id", unique = true)
    private String clientId;

    @Column(name = "secret")
    @JsonIgnore
    private String clientSecret;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<RedirectUrl> redirectUrls;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ClientScope> scopes;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<AuthenticationMethod> authenticationMethods;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<AuthorizationGrantType> authorizationGrantTypes;
}
