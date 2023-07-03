package com.roboskeletron.authentication_server.controller;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.exception.InvalidPasswordException;
import com.roboskeletron.authentication_server.exception.SamePasswordException;
import com.roboskeletron.authentication_server.service.UserService;
import com.roboskeletron.authentication_server.util.PasswordValidator;
import com.roboskeletron.authentication_server.util.SetMapper;
import com.roboskeletron.authentication_server.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<User> getUser(@PathVariable int id){
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<User> getUser(@PathVariable String username){
        User user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<Page<User>> getUsers(@RequestParam("pageIndex") int pageIndex,
                                               @RequestParam("pageSize") int pageSize,
                                               @RequestParam("sortBy") String sortBy){
        sortBy = sortBy == null ? "id" : sortBy;

        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy));

        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/create")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<User> createUser(@RequestParam("username") String username,
                                           @RequestParam("password") String password,
                                           @RequestParam("authorities") Set<String> authorities){

        if (!PasswordValidator.isPasswordValid(password))
            throw new InvalidPasswordException();

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .userAuthorities(SetMapper.mapObjectToSet(
                        String::toString,
                        UserMapper.getAuthorityFunc(),
                        authorities
                )).build();

        user = userService.createUser(user);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/change_password")
    public ResponseEntity<User> changePassword(@AuthenticationPrincipal Jwt jwt,
                                           @RequestParam("password") String password,
                                           @RequestParam("new_password") String new_password){
        String username = jwt.getSubject();
        User user = userService.getUser(username);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Invalid password");

        if (!PasswordValidator.isPasswordValid(new_password))
            throw new InvalidPasswordException();

        if (passwordEncoder.matches(new_password, user.getPassword()))
            throw  new SamePasswordException();

        user.setPassword(new_password);

        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<User> deleteUser(@RequestParam("id") int id, @AuthenticationPrincipal Jwt jwt){
        User admin = userService.getUser(jwt.getSubject());
        User user = userService.getUser(id);

        deleteUser(admin, user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<User> deleteUser(@RequestParam("username") String username,
                                           @AuthenticationPrincipal Jwt jwt){
        User admin = userService.getUser(jwt.getSubject());
        User user = userService.getUser(username);

        deleteUser(admin, user);

        return ResponseEntity.ok(user);
    }

    private void deleteUser(User agent, User target){

    }
}
