package com.roboskeletron.authentication_server.dto;

import com.roboskeletron.authentication_server.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private User user;
    private String password;
}
