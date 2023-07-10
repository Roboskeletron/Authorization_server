package com.roboskeletron.authentication_server.controller;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.dto.UserDTO;
import com.roboskeletron.authentication_server.exception.InvalidPasswordException;
import com.roboskeletron.authentication_server.exception.ParameterRequiredException;
import com.roboskeletron.authentication_server.exception.SamePasswordException;
import com.roboskeletron.authentication_server.security.PasswordGenerator;
import com.roboskeletron.authentication_server.security.PasswordValidator;
import com.roboskeletron.authentication_server.service.UserService;
import com.roboskeletron.authentication_server.util.SetMapper;
import com.roboskeletron.authentication_server.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    @GetMapping
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<User> getUser(@RequestParam(required = false) Integer id,
                                        @RequestParam(required = false) String username) {
        return ResponseEntity.ok(getTarget(id, username));
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<Page<User>> getUsers(@RequestParam int pageIndex,
                                               @RequestParam int pageSize,
                                               @RequestParam(required = false) String sortBy) {
        sortBy = sortBy == null ? "id" : sortBy;

        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy));

        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/create")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<UserDTO> createUser(@RequestParam String username,
                                              @RequestParam(required = false) String password,
                                              @RequestParam Set<String> authorities,
                                              @AuthenticationPrincipal Jwt jwt) {
        if (password == null)
            password = generatePassword();
        else if (!PasswordValidator.isPasswordValid(password))
            throw new InvalidPasswordException();

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .userAuthorities(SetMapper.mapObjectToSet(
                        String::toString,
                        UserMapper.getAuthorityFunc(),
                        authorities
                )).build();

        validateAuthorities(jwt, user);

        return ResponseEntity.ok(new UserDTO(userService.createUser(user), password));
    }

    @PostMapping("/password/change")
    public ResponseEntity<User> changePassword(@AuthenticationPrincipal Jwt jwt,
                                               @RequestParam("password") String password,
                                               @RequestParam("new_password") String new_password) {
        String username = jwt.getSubject();
        User user = userService.getUser(username);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Invalid password");

        if (!PasswordValidator.isPasswordValid(new_password))
            throw new InvalidPasswordException();

        if (passwordEncoder.matches(new_password, user.getPassword()))
            throw new SamePasswordException();

        user.setPassword(new_password);

        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<User> deleteUser(@RequestParam(required = false) Integer id,
                                           @RequestParam(required = false) String username,
                                           @AuthenticationPrincipal Jwt jwt) {
        User target = getTarget(id, username);

        validateAuthorities(jwt, target);
        userService.deleteUser(target);

        return ResponseEntity.ok(target);
    }

    @PostMapping("/password/reset")
    @PreAuthorize("hasAnyAuthority('super_user', 'admin')")
    public ResponseEntity<String> resetPassword(@RequestParam(required = false) Integer id,
                                                @RequestParam(required = false) String username,
                                                @AuthenticationPrincipal Jwt jwt) {
        User target = getTarget(id, username);

        validateAuthorities(jwt, target);

        String password = generatePassword();
        target.setPassword(passwordEncoder.encode(password));

        return ResponseEntity.ok(password);
    }

    @PostMapping("/authorities/grant")
    @PreAuthorize("hasAuthority('super_user')")
    public ResponseEntity<User> grantAuthorities(@RequestParam(required = false) Integer id,
                                                 @RequestParam(required = false) String username,
                                                 @RequestParam Set<String> authorities,
                                                 @AuthenticationPrincipal Jwt jwt){
        User target = getTarget(id, username);

        var userAuthorities = SetMapper.mapFromStrings(UserMapper.getAuthorityFunc(),
                authorities.toArray(new String[0]));

        userAuthorities.forEach(authority -> userService.grantAuthority(target, authority));

        validateAuthorities(jwt, target);

        return ResponseEntity.ok(userService.updateUser(target));
    }

    @PostMapping("/authorities/revoke")
    @PreAuthorize("hasAuthority('super_user')")
    public ResponseEntity<User> revokeAuthorities(@RequestParam(required = false) Integer id,
                                                 @RequestParam(required = false) String username,
                                                 @RequestParam Set<String> authorities,
                                                 @AuthenticationPrincipal Jwt jwt){
        User target = getTarget(id, username);

        validateAuthorities(jwt, target);

        authorities.forEach(authority -> userService.revokeAuthority(target, authority));

        return ResponseEntity.ok(userService.updateUser(target));
    }

    private void validateAuthorities(Jwt agent, User target) {
        var targetAuthorities = UserMapper.getAuthoritiesAsSet(target);
        var agentAuthorities = (Set<String>) agent.getClaims().get("authorities");

        if (targetAuthorities.contains("super_user"))
            throw new AccessDeniedException("Cant apply this action to super_user");

        if (!agentAuthorities.contains("super_user") && targetAuthorities.contains("admin"))
            throw new AccessDeniedException("Only super_user can apply actions to admin");
    }

    private String generatePassword() {
        PasswordGenerator passwordGenerator =
                new PasswordGenerator(PasswordValidator.getPasswordPattern());

        String password = passwordGenerator.generate();

        if (!PasswordValidator.isPasswordValid(password))
            throw new InvalidPasswordException();

        return password;
    }

    private User getTarget(Integer id, String username) {
        if (id != null)
            return userService.getUser(id);
        else if (username != null)
            return userService.getUser(username);

        throw new ParameterRequiredException("Require id or username");
    }
}
