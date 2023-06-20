package com.roboskeletron.authentication_server.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "redirect_urls")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedirectUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String url;

    @ManyToOne
    private Client client;
}
