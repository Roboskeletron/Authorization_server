package com.roboskeletron.authentication_server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.roboskeletron.authentication_server.domain.Client;
import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.security.JpaRegisteredClientRepository;
import com.roboskeletron.authentication_server.security.JpaUserDetailsManager;
import com.roboskeletron.authentication_server.service.ClientService;
import com.roboskeletron.authentication_server.service.UserService;
import com.roboskeletron.authentication_server.security.JwtAuthoritiesConverter;
import com.roboskeletron.authentication_server.security.JwtSubjectConverter;
import com.roboskeletron.authentication_server.util.ClientMapper;
import com.roboskeletron.authentication_server.util.SetMapper;
import com.roboskeletron.authentication_server.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
    private final UserService userService;
    private final ClientService clientService;

    @Value("${spring.security.user.name}")
    private String suName;
    @Value("${spring.security.user.password}")
    private String suPassword;
    @Value("${spring.security.user.authorities}")
    private String suAuthorities;
    @Value("${spring.security.client.clientId}")
    private String clientId;
    @Value("${spring.security.client.secret}")
    private String clientSecret;
    @Value("${spring.security.client.scopes}")
    private Set<String> scopes;
    @Value("${spring.security.client.auth-methods}")
    private Set<String> authMethods;
    @Value("${spring.security.client.grant-types}")
    private Set<String> grantTypes;
    @Value("${spring.security.client.redirect-urls}")
    private Set<String> redirectUrls;
    private KeyPair keyPair = null;

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

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(Customizer.withDefaults());

        http.authorizeHttpRequests(c -> c.anyRequest().authenticated())
                .oauth2ResourceServer(server -> server
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())));

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
    public RegisteredClientRepository registeredClientRepository() {
        var registeredClientRepository = new JpaRegisteredClientRepository(clientService);

        if (!clientService.doesClientExists(clientId)){
            Client client = Client.builder()
                    .clientId(clientId)
                    .clientSecret(passwordEncoder().encode(clientSecret))
                    .scopes(SetMapper.mapFromStrings(
                            ClientMapper.getScopeFunc(), scopes.toArray(new String[0])))
                    .authenticationMethods(SetMapper.mapFromStrings(
                            ClientMapper.getAuthMethodFunc(), authMethods.toArray(new String[0])
                    ))
                    .authorizationGrantTypes(SetMapper.mapFromStrings(
                            ClientMapper.getGrantTypeFunc(), grantTypes.toArray(new String[0])
                    ))
                    .redirectUrls(SetMapper.mapFromStrings(
                            ClientMapper.getRedirectUrlFunc(), redirectUrls.toArray(new String[0])
                    )).build();

            clientService.createClient(client);
        }

        clientSecret = null;

        return registeredClientRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var detailsManager = new JpaUserDetailsManager(userService);

        if (!userService.doesUserExists(suName)) {
            var encoder = passwordEncoder();

            User user = User.builder()
                    .username(suName)
                    .password(encoder.encode(suPassword))
                    .userAuthorities(SetMapper.mapFromStrings(UserMapper.getDefaultAuthorityFunc(),
                            suAuthorities.split(" ")))
                    .build();

            detailsManager.createUser(user);
        }

        suPassword = null;

        return detailsManager;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        log.info("Generating RSA key");
        KeyPair keyPair = getKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        log.info("Creating JWT decoder");
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withPublicKey((RSAPublicKey) getKeyPair().getPublic()).build();

        JwtSubjectConverter subjectConverter = new JwtSubjectConverter();
        JwtAuthoritiesConverter authoritiesConverter = new JwtAuthoritiesConverter(userService,
                subjectConverter);

        Map<String, Converter<Object, ?>> claimConverters = new HashMap<>();

        claimConverters.put(JwtClaimNames.SUB, subjectConverter);
        claimConverters.put("authorities", authoritiesConverter);

        MappedJwtClaimSetConverter converter = MappedJwtClaimSetConverter
                .withDefaults(claimConverters);

        decoder.setClaimSetConverter(converter);

        return decoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    public KeyPair getKeyPair(){
        if (keyPair == null){
            keyPair = generateRsaKey();
        }

        return keyPair;
    }

    private KeyPair generateRsaKey(){
        log.info("Creating RSA key pair");
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}
