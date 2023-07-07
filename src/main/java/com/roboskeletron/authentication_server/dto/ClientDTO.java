package com.roboskeletron.authentication_server.dto;

import com.roboskeletron.authentication_server.domain.Client;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDTO {
    private Client client;
    private String secret;
}
