package com.roboskeletron.authentication_server.dto;

import com.roboskeletron.authentication_server.domain.User;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Getter
public class UserDTO {
    private final User user;
    private final String password;
}
