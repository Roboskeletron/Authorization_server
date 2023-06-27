package com.roboskeletron.authentication_server.controller;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manage/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<UserDetails> getUser(@PathVariable int id){
        //TODO implement proper conversion to json
        UserDetails user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}
