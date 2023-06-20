package com.roboskeletron.authentication_server.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "client_scopes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientScope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Client client;
}
