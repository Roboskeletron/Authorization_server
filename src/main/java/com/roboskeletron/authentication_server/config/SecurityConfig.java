package com.roboskeletron.authentication_server.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.roboskeletron.authentication_server.domain.*;
import com.roboskeletron.authentication_server.security.JpaRegisteredClientRepository;
import com.roboskeletron.authentication_server.security.JpaUserDetailsManager;
import com.roboskeletron.authentication_server.service.*;
import com.roboskeletron.authentication_server.util.ClientBuilder;
import com.roboskeletron.authentication_server.util.UserBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserService userService;
    private final UserScopeService userScopeService;
    private final ClientService clientService;
    private final ClientScopeService clientScopeService;
    private final AuthorizationGrantTypeService authorizationGrantTypeService;
    private final AuthenticationMethodService authenticationMethodService;
    private final RedirectUrlService redirectUrlService;
    private final JWKSource<SecurityContext> jwkSource;

    @Bean
    @Order(1)
    public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.exceptionHandling(
                c -> c.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
        );

        http.oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(Customizer.withDefaults());

        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(){
        var clientRepository = new JpaRegisteredClientRepository(clientService,
                clientScopeService,
                authenticationMethodService,
                authorizationGrantTypeService,
                redirectUrlService);

        if (!clientService.doesClientExists("Client"))
        {
            AuthenticationMethod method = authenticationMethodService.getAuthMethod(1);
            AuthorizationGrantType grantType = authorizationGrantTypeService.getGrantType(1);
            RedirectUrl url = redirectUrlService.getRedirectUrl(1);

            Client client = new ClientBuilder()
                    .setClientId("Client")
                    .setSecret("{bcrypt}$2y$10$aDkfe9wxeJg0yL19oXzOoeM1ibqxfImOv6ww7Euk0w//HaftfgAka")
                    .setRedirectUrl(url)
                    .setAuthenticationMethods(method)
                    .setAuthorizationGrantTypes(grantType).build();
            clientService.createClient(client);
        }

        return clientRepository;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        var detailsManager = new JpaUserDetailsManager(userService, userScopeService);

        if (!userService.doesUserExists("User")){
            User user = new UserBuilder()
                    .setUsername("User")
                    .setPassword("{bcrypt}$2y$10$smjI91vJ4TBUuI777c4zSOA8nKTR/jKrFOYgzKwB26q8NzrYaOq66")
                    .setScopes(userScopeService.getUserScope(1))
                    .build();
            userService.createUser(user);
        }

        return detailsManager;
    }

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(){
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(jwtTokenCustomizer());

        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(){
        return context -> {
            var claims = context.getClaims();
            Set<String> authorities = context.getPrincipal().getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

            claims.claim("user scopes", authorities);
        };
    }
}
