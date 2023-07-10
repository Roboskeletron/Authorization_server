package com.roboskeletron.authentication_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "redirect_urls")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedirectUrl implements ClientProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String url;

    @ManyToOne
    @JsonIgnore
    private Client client;

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RedirectUrl redirectUrl)
            return url.equals(redirectUrl.getUrl());

        return false;
    }
}
