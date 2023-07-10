package com.roboskeletron.authentication_server.controller;

import com.roboskeletron.authentication_server.domain.*;
import com.roboskeletron.authentication_server.dto.ClientDTO;
import com.roboskeletron.authentication_server.exception.InvalidPasswordException;
import com.roboskeletron.authentication_server.exception.ParameterRequiredException;
import com.roboskeletron.authentication_server.exception.SamePasswordException;
import com.roboskeletron.authentication_server.security.PasswordGenerator;
import com.roboskeletron.authentication_server.security.PasswordValidator;
import com.roboskeletron.authentication_server.service.ClientService;
import com.roboskeletron.authentication_server.util.ClientMapper;
import com.roboskeletron.authentication_server.util.SetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('super_user', 'admin')")
public class ClientController {
    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<Client> getClient(@RequestParam(required = false) Integer id,
                                            @RequestParam(required = false) String clientId) {
        return ResponseEntity.ok(getTargetClient(id, clientId));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Client>> getClients(@RequestParam int pageIndex,
                                                   @RequestParam int pageSize,
                                                   @RequestParam(required = false) String sortBy) {
        sortBy = sortBy == null ? "id" : sortBy;

        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy));

        Page<Client> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/create")
    public ResponseEntity<ClientDTO> create(@RequestParam String clientId,
                                            @RequestParam(required = false) String secret,
                                            @RequestParam Set<String> scopes,
                                            @RequestParam Set<String> authorizationMethods,
                                            @RequestParam Set<String> authorizationGrantTypes,
                                            @RequestParam Set<String> redirectUrls) {
        if (secret == null)
            secret = generatePassword();
        else if (!PasswordValidator.isPasswordValid(secret))
            throw new InvalidPasswordException();

        Client client = Client.builder()
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(secret))
                .scopes(SetMapper.mapFromStrings(ClientMapper.getScopeFunc(), scopes.toArray(new String[0])))
                .authenticationMethods(SetMapper.mapFromStrings(ClientMapper.getAuthMethodFunc(),
                        authorizationMethods.toArray(new String[0])))
                .authorizationGrantTypes(SetMapper.mapFromStrings(ClientMapper.getGrantTypeFunc(),
                        authorizationGrantTypes.toArray(new String[0])))
                .redirectUrls(SetMapper.mapFromStrings(ClientMapper.getRedirectUrlFunc(),
                        redirectUrls.toArray(new String[0])))
                .build();

        return ResponseEntity.ok(new ClientDTO(clientService.createClient(client), secret));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Client> delete(@RequestParam(required = false) Integer id,
                                         @RequestParam(required = false) String clientId) {
        Client client = getTargetClient(id, clientId);
        clientService.deleteClient(client);

        return ResponseEntity.ok(client);
    }

    @PostMapping("/scopes/grant")
    public ResponseEntity<Client> grantScopes(@RequestParam(required = false) Integer id,
                                              @RequestParam(required = false) String clientId,
                                              @RequestParam Set<String> scopes){
        Client client = getTargetClient(id, clientId);

        Set<ClientScope> clientScopes = SetMapper.mapFromStrings(ClientMapper.getScopeFunc(),
                scopes.toArray(new String[0]));

        clientScopes.forEach(scope -> clientService.grantProperty(client, scope));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("scopes/revoke")
    public ResponseEntity<Client> revokeScopes(@RequestParam(required = false) Integer id,
                                               @RequestParam(required = false) String clientId,
                                               @RequestParam Set<String> scopes){
        Client client = getTargetClient(id, clientId);

        Set<ClientScope> clientScopes = SetMapper.mapFromStrings(ClientMapper.getScopeFunc(),
                scopes.toArray(new String[0]));

        clientScopes.forEach(scope -> clientService.revokeProperty(client, scope));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("authentication_methods/grant")
    public ResponseEntity<Client> grantAuthenticationMethods(@RequestParam(required = false) Integer id,
                                                             @RequestParam(required = false) String clientId,
                                                             @RequestParam Set<String> methods){
        Client client = getTargetClient(id, clientId);

        Set<AuthenticationMethod> authenticationMethods = SetMapper.mapFromStrings(ClientMapper.getAuthMethodFunc(),
                methods.toArray(new String[0]));

        authenticationMethods.forEach(scope -> clientService.grantProperty(client, scope));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("authentication_methods/revoke")
    public ResponseEntity<Client> revokeAuthenticationMethods(@RequestParam(required = false) Integer id,
                                                             @RequestParam(required = false) String clientId,
                                                             @RequestParam Set<String> methods){
        Client client = getTargetClient(id, clientId);

        Set<AuthenticationMethod> authenticationMethods = SetMapper.mapFromStrings(ClientMapper.getAuthMethodFunc(),
                methods.toArray(new String[0]));

        authenticationMethods.forEach(scope -> clientService.revokeProperty(client, scope));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("grant_types/grant")
    public ResponseEntity<Client> grantGrantTypes(@RequestParam(required = false) Integer id,
                                                             @RequestParam(required = false) String clientId,
                                                             @RequestParam Set<String> grantTypes){
        Client client = getTargetClient(id, clientId);

        Set<AuthorizationGrantType> authenticationMethods = SetMapper.mapFromStrings(ClientMapper.getGrantTypeFunc(),
                grantTypes.toArray(new String[0]));

        authenticationMethods.forEach(scope -> clientService.grantProperty(client, scope));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("grant_types/revoke")
    public ResponseEntity<Client> revokeGrantTypes(@RequestParam(required = false) Integer id,
                                                              @RequestParam(required = false) String clientId,
                                                              @RequestParam Set<String> grantTypes){
        Client client = getTargetClient(id, clientId);

        Set<AuthorizationGrantType> authorizationGrantTypes = SetMapper.mapFromStrings(ClientMapper.getGrantTypeFunc(),
                grantTypes.toArray(new String[0]));

        authorizationGrantTypes.forEach(scope -> clientService.revokeProperty(client, scope));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("redirect_urls/grant")
    public ResponseEntity<Client> grantRedirectUrls(@RequestParam(required = false) Integer id,
                                                  @RequestParam(required = false) String clientId,
                                                  @RequestParam Set<String> urls){
        Client client = getTargetClient(id, clientId);

        Set<RedirectUrl> authenticationMethods = SetMapper.mapFromStrings(ClientMapper.getRedirectUrlFunc(),
                urls.toArray(new String[0]));

        authenticationMethods.forEach(scope -> clientService.grantProperty(client, scope));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("redirect_urls/revoke")
    public ResponseEntity<Client> revokeRedirectUrls(@RequestParam(required = false) Integer id,
                                                   @RequestParam(required = false) String clientId,
                                                   @RequestParam Set<String> urls){
        Client client = getTargetClient(id, clientId);

        Set<RedirectUrl> authorizationGrantTypes = SetMapper.mapFromStrings(ClientMapper.getRedirectUrlFunc(),
                urls.toArray(new String[0]));

        authorizationGrantTypes.forEach(scope -> clientService.revokeProperty(client, scope));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("/secret/change")
    public ResponseEntity<Client> changeSecret(@RequestParam(required = false) Integer id,
                                               @RequestParam(required = false) String clientId,
                                               @RequestParam String newSecret){
        Client client = getTargetClient(id, clientId);

        if (passwordEncoder.matches(newSecret, client.getClientSecret()))
            throw  new SamePasswordException();

        if (!PasswordValidator.isPasswordValid(newSecret))
            throw new InvalidPasswordException();

        client.setClientSecret(passwordEncoder.encode(newSecret));

        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PostMapping("secret/reset")
    public ResponseEntity<ClientDTO> resetSecret(@RequestParam(required = false) Integer id,
                                                 @RequestParam(required = false) String clientId){
        Client client = getTargetClient(id, clientId);
        String secret = generatePassword();

        client.setClientSecret(passwordEncoder.encode(secret));

        return ResponseEntity.ok(new ClientDTO(client, secret));
    }

    private Client getTargetClient(Integer id, String clientId) {
        if (id != null)
            return clientService.getClient(id);

        if (clientId != null)
            return clientService.getClient(clientId);

        throw new ParameterRequiredException("id or clientId required");
    }

    private String generatePassword() {
        PasswordGenerator generator = new PasswordGenerator(PasswordValidator.getPasswordPattern());
        String password = generator.generate();

        if (!PasswordValidator.isPasswordValid(password))
            throw new InvalidPasswordException();

        return password;
    }
}
