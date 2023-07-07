package com.roboskeletron.authentication_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "authorization_grant_types")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationGrantType implements ClientProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JsonIgnore
    private Client client;
}
