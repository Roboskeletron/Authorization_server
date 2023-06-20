package com.roboskeletron.authentication_server.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "authorization_grant_types")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationGrantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Client client;
}
