package com.roboskeletron.authentication_server.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@Table(name = "user_authorities")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JsonIgnore
    private User user;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return name;
    }
}
