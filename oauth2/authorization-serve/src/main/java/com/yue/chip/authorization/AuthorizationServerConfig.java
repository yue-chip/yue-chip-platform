package com.yue.chip.authorization;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.yue.chip.authorization.password.AccessToken;
import com.yue.chip.authorization.password.OAuth2PasswordCredentialsAuthenticationConverter;
import com.yue.chip.authorization.password.OAuth2PasswordCredentialsAuthenticationProvider;
import com.yue.chip.core.ResultData;
import com.yue.chip.core.common.enums.ResultDataState;
import jakarta.annotation.Resource;
import jakarta.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@ConditionalOnClass(value = {EnableWebSecurity.class, Servlet.class})
public class AuthorizationServerConfig {

    @Autowired
    private JdbcTemplate  jdbcTemplate;
    @DubboReference
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final HttpMessageConverter<Object> responseConverter = new MappingJackson2HttpMessageConverter();

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer = http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0
        oAuth2AuthorizationServerConfigurer.authorizationEndpoint(authorizationEndpoint -> {
            authorizationEndpoint.errorResponseHandler((request, response, exception) -> {
                ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
                httpResponse.setStatusCode(HttpStatus.OK);
                ResultData resultData = ResultData.builder().status(ResultDataState.ERROR.getKey()).message(exception.getMessage()).build();
                responseConverter.write(resultData, MediaType.APPLICATION_JSON_UTF8,httpResponse);
            });
        });
        oAuth2AuthorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> {
            tokenEndpoint.accessTokenRequestConverter(new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2AuthorizationCodeAuthenticationConverter(), // 授权码模式
                new OAuth2RefreshTokenAuthenticationConverter(),  // 刷新token
                new OAuth2ClientCredentialsAuthenticationConverter(),  // 客户端模式
                new OAuth2PasswordCredentialsAuthenticationConverter() //密码模式
            )));
            tokenEndpoint.errorResponseHandler((request, response, exception) -> {
                ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
                httpResponse.setStatusCode(HttpStatus.OK);
                ResultData resultData = ResultData.builder().status(ResultDataState.ERROR.getKey()).message(exception.getMessage()).build();
                responseConverter.write(resultData, MediaType.APPLICATION_JSON_UTF8,httpResponse);
            });
            tokenEndpoint.accessTokenResponseHandler((request, response, authentication) -> {
                ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
                httpResponse.setStatusCode(HttpStatus.OK);
                OAuth2AccessTokenAuthenticationToken auth2AccessTokenAuthenticationToken = (OAuth2AccessTokenAuthenticationToken)authentication;
                AccessToken accessToken = AccessToken.builder()
                        .access_token(auth2AccessTokenAuthenticationToken.getAccessToken().getTokenValue())
                        .refresh_token(auth2AccessTokenAuthenticationToken.getRefreshToken().getTokenValue())
                        .scope(StringUtils.join(auth2AccessTokenAuthenticationToken.getRegisteredClient().getScopes().toArray(), ","))
                        .toke_type(auth2AccessTokenAuthenticationToken.getAccessToken().getTokenType().getValue())
                        .expires_in(ChronoUnit.SECONDS.between(auth2AccessTokenAuthenticationToken.getAccessToken().getIssuedAt(), auth2AccessTokenAuthenticationToken.getAccessToken().getExpiresAt()))
                        .build();
                ResultData resultData = ResultData.builder().data(accessToken).build();
                responseConverter.write(resultData, MediaType.APPLICATION_JSON_UTF8,httpResponse);
            });
        });
        oAuth2AuthorizationServerConfigurer.clientAuthentication(authenticationConfigurer -> {
            authenticationConfigurer.errorResponseHandler((request, response, exception) -> {
                ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
                httpResponse.setStatusCode(HttpStatus.OK);
                ResultData resultData = ResultData.builder().status(ResultDataState.ERROR.getKey()).message(exception.getMessage()).build();
                responseConverter.write(resultData, MediaType.APPLICATION_JSON_UTF8,httpResponse);
            });
        });
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        SecurityFilterChain securityFilterChain = http.build();
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new OAuth2PasswordCredentialsAuthenticationProvider(http.getSharedObject(OAuth2AuthorizationService.class),http.getSharedObject(OAuth2TokenGenerator.class),userDetailsService));
        return securityFilterChain;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

//        RSAKey rsaKey = null;
//        try {
//
//            rsaKey = RSAKey.parse("{\n" +
//                    "    \"keys\": [\n" +
//                    "        {\n" +
//                    "            \"kty\": \"RSA\",\n" +
//                    "            \"x5t#S256\": \"VFGQxCmgHIh_gaF-wPb1xC9oKA1w5la0EFmqqPMrqmw\",\n" +
//                    "            \"e\": \"AQAB\",\n" +
//                    "            \"kid\": \"felordcn\",\n" +
//                    "            \"x5c\": [\n" +
//                    "\"MIIDczCCAlugAwIBAgIEURwmYzANBgkqhkiG9w0BAQsFADBqMQ0wCwYDVQQGEwQoY24pMQ0wCwYDVQQIEwQoaG4pMQ0wCwYDVQQHEwQoenopMRMwEQYDVQQKEwooZmVsb3JkY24pMRMwEQYDVQQLEwooZmVsb3JkY24pMREwDwYDVQQDEwgoRmVsb3JkKTAeFw0yMTEwMTgwNDUwMTRaFw0yMjEwMTgwNDUwMTRaMGoxDTALBgNVBAYTBChjbikxDTALBgNVBAgTBChobikxDTALBgNVBAcTBCh6eikxEzARBgNVBAoTCihmZWxvcmRjbikxEzARBgNVBAsTCihmZWxvcmRjbikxETAPBgNVBAMTCChGZWxvcmQpMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgo0TPk1td7iROmmLcGbOsZ2F68kTertDwRyk+leqBl+qyJAkjoVgVaCRRQHZmvu/YGp93vOaEd/zFdVj/rFvMXmwBxgYPOeSG0bHkYtFBaUiLf1vhW5lyiPHcGide3uw1p+il3JNiOpcnLCbAKZgzm4qaugeuOD02/M0YcMW2Jqg3SUWpC+9vu9yt5dVc1xpmpwEAamKzvynI3Zxl44ddlA8RRAS6kV0OUcKbEG63G3yZ4MHnhrFrZDuvlwfSSgn0wFOC/b6mJ+bUxByMAXKD0d4DS2B2mVl7RO5AzL4SFcqtZZE3Drtcli67bsENyOQeoTVaKO6gu5PEEFlQ7pHKwIDAQABoyEwHzAdBgNVHQ4EFgQUqbLZ76DtdEEpTZUcgP7LsdGk8/AwDQYJKoZIhvcNAQELBQADggEBAEzfhi6/B00NxSPKAYJea/0MIyHr4X8Ue6Du+xl2q0UFzSkyIiMcPNmOYW5L1PWGjxR5IIiUgxKI5bEvhLkHhkMV+GRQSPKJXeC3szHdoZ3fXDZnuC0I88a1YDsvzuVhyjLL/n5lETRT4QWo5LsAj5v7AX+p46HM0iqSyTptafm2wheEosFA3ro4+vEDRaMrKLY1KdJuvvrrQIRpplhB/JbncmjWrEEvICSLEXm+kdGFgWNXkNxF0PhTLyPu3tEb2xLmjFltALwi3KPUGv9zVjxb7KyYiMnVsOPnwpDLOyREM9j4RLDiwf0tsCqPqltVExvFZouoL26fhcozfcrqC70=\"\n" +
//                    "            ],\n" +
//                    "            \"n\": \"go0TPk1td7iROmmLcGbOsZ2F68kTertDwRyk-leqBl-qyJAkjoVgVaCRRQHZmvu_YGp93vOaEd_zFdVj_rFvMXmwBxgYPOeSG0bHkYtFBaUiLf1vhW5lyiPHcGide3uw1p-il3JNiOpcnLCbAKZgzm4qaugeuOD02_M0YcMW2Jqg3SUWpC-9vu9yt5dVc1xpmpwEAamKzvynI3Zxl44ddlA8RRAS6kV0OUcKbEG63G3yZ4MHnhrFrZDuvlwfSSgn0wFOC_b6mJ-bUxByMAXKD0d4DS2B2mVl7RO5AzL4SFcqtZZE3Drtcli67bsENyOQeoTVaKO6gu5PEEFlQ7pHKw\"\n" +
//                    "        }\n" +
//                    "    ]\n" +
//                    "}");
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
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

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    public OAuth2AuthorizationConsentService authorizationConsentService(RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate,registeredClientRepository);
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
