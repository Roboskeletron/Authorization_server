package com.roboskeletron.authentication_server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "authorization_grant_types")
public class AuthorizationGrantType  implements com.roboskeletron.authentication_server.util.Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Client client;
}
