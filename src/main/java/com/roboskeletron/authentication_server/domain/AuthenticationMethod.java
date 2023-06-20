package com.roboskeletron.authentication_server.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "authentication_methods")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Client client;
}
