package com.yue.chip.authorization;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.yue.chip.authorization.client.YueChipOAuth2ClientCredentialsAuthenticationConverter;
import com.yue.chip.authorization.password.AccessToken;
import com.yue.chip.authorization.password.OAuth2PasswordCredentialsAuthenticationConverter;
import com.yue.chip.authorization.password.OAuth2PasswordCredentialsAuthenticationProvider;
import com.yue.chip.core.ResultData;
import com.yue.chip.core.common.enums.ResultDataState;
import jakarta.annotation.Resource;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
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
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.text.ParseException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@ConditionalOnClass(value = {EnableWebSecurity.class, Servlet.class})
public class AuthorizationServerConfig {

    @Resource
    private JdbcTemplate  jdbcTemplate;
    @DubboReference
    private UserDetailsService userDetailsService;
    @Resource
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
                ResultData resultData = ResultData.builder().status(ResultDataState.ERROR.getKey()).message(exception.getMessage()).build();
                responseWrite(response,resultData);
            });
        });
        oAuth2AuthorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> {
            tokenEndpoint.accessTokenRequestConverter(new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2AuthorizationCodeAuthenticationConverter(), // 授权码模式
                new OAuth2RefreshTokenAuthenticationConverter(),  // 刷新token
//                new OAuth2ClientCredentialsAuthenticationConverter(),  // 客户端模式
                new YueChipOAuth2ClientCredentialsAuthenticationConverter(), // 自定义客户端模式 多租户模式
                new OAuth2PasswordCredentialsAuthenticationConverter() //自定义密码模式
            )));
            tokenEndpoint.errorResponseHandler((request, response, exception) -> {
                ResultData resultData = ResultData.builder().status(ResultDataState.ERROR.getKey()).message(exception.getMessage()).build();
                responseWrite(response,resultData);
            });
            tokenEndpoint.accessTokenResponseHandler((request, response, authentication) -> {
                OAuth2AccessTokenAuthenticationToken token = (OAuth2AccessTokenAuthenticationToken)authentication;
                AccessToken accessToken = AccessToken.builder()
                        .access_token(token.getAccessToken().getTokenValue())
                        .refresh_token(token.getRefreshToken().getTokenValue())
                        .toke_type(token.getAccessToken().getTokenType().getValue())
                        .expires_in(ChronoUnit.SECONDS.between(token.getAccessToken().getIssuedAt(), token.getAccessToken().getExpiresAt()))
                        .build();
                ResultData resultData = ResultData.builder().data(accessToken).build();
                responseWrite(response,resultData);
            });
        });
        oAuth2AuthorizationServerConfigurer.clientAuthentication(authenticationConfigurer -> {
            authenticationConfigurer.errorResponseHandler((request, response, exception) -> {
                ResultData resultData = ResultData.builder().status(ResultDataState.ERROR.getKey()).message(exception.getMessage()).build();
                responseWrite(response,resultData);
            });
        });
        oAuth2AuthorizationServerConfigurer.tokenRevocationEndpoint(authenticationConfigurer -> {
            authenticationConfigurer.errorResponseHandler((request, response, exception) -> {
                ResultData resultData = ResultData.builder().status(ResultDataState.ERROR.getKey()).message(exception.getMessage()).build();
                responseWrite(response,resultData);
            });
            authenticationConfigurer.revocationResponseHandler((request, response, authentication) -> {
                ResultData resultData = ResultData.builder().build();
                responseWrite(response,resultData);
            });
        });
        http.sessionManagement(sessionManagementConfigurer -> {
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
//        似乎被干掉了，Endpoint没有Logout =》 AuthorizationServerSettings
//        http.logout(Logout -> {
//            Logout.clearAuthentication(true)
//                    .invalidateHttpSession(true)
//                    .logoutSuccessHandler((request, response, authentication) -> {
//                        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
//                        httpResponse.setStatusCode(HttpStatus.OK);
//                        ResultData resultData = ResultData.builder().build();
//                        responseConverter.write(resultData, MediaType.APPLICATION_JSON_UTF8,httpResponse);
//                    })
//                .permitAll();
//        });

        SecurityFilterChain securityFilterChain = http.build();

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new OAuth2PasswordCredentialsAuthenticationProvider(http.getSharedObject(OAuth2AuthorizationService.class),http.getSharedObject(OAuth2TokenGenerator.class), userDetailsService, passwordEncoder));
        return securityFilterChain;
    }

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
//        return context -> {
//            JwtClaimsSet.Builder claims = context.getClaims();
//            claims.claim(OAuth2ParameterNames.USERNAME, new StringBuffer(""));
//            claims.claim(CurrentUserUtil.TOKEN,new StringBuffer(""));
//        };
//    }
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
//        KeyPair keyPair = generateRsaKey();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        RSAKey rsaKey = new RSAKey.Builder(publicKey)
//                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
//                .build();

        RSAKey rsaKey = null;
        try {
            rsaKey = RSAKey.parse("{\"kty\":\"RSA\",\"n\":\"0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx4cbbfAAtVT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMstn64tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FDW2QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n91CbOpbISD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINHaQ-G_xBniIqbw0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw\",\"e\":\"AQAB\",\"d\":\"X4cTteJY_gn4FYPsXB8rdXix5vwsg1FLN5E3EaG6RJoVH-HLLKD9M7dx5oo7GURknchnrRweUkC7hT5fJLM0WbFAKNLWY2vv7B6NqXSzUvxT0_YSfqijwp3RTzlBaCxWp4doFk5N2o8Gy_nHNKroADIkJ46pRUohsXywbReAdYaMwFs9tv8d_cPVY3i07a3t8MN6TNwm0dSawm9v47UiCl3Sk5ZiG7xojPLu4sbg1U2jx4IBTNBznbJSzFHK66jT8bgkuqsk0GjskDJk19Z4qwjwbsnn4j2WBii3RL-Us2lGVkY8fkFzme1z0HbIkfz0Y6mqnOYtqc0X4jfcKoAC8Q\",\"p\":\"83i-7IvMGXoMXCskv73TKr8637FiO7Z27zv8oj6pbWUQyLPQBQxtPVnwD20R-60eTDmD2ujnMt5PoqMrm8RfmNhVWDtjjMmCMjOpSXicFHj7XOuVIYQyqVWlWEh6dN36GVZYk93N8Bc9vY41xy8B9RzzOGVQzXvNEvn7O0nVbfs\",\"q\":\"3dfOR9cuYq-0S-mkFLzgItgMEfFzB2q3hWehMuG0oCuqnb3vobLyumqjVZQO1dIrdwgTnCdpYzBcOfW5r370AFXjiWft_NGEiovonizhKpo9VVS78TzFgxkIdrecRezsZ-1kYd_s1qDbxtkDEgfAITAG9LUnADun4vIcb6yelxk\",\"dp\":\"G4sPXkc6Ya9y8oJW9_ILj4xuppu0lzi_H7VTkS8xj5SdX3coE0oimYwxIi2emTAue0UOa5dpgFGyBJ4c8tQ2VF402XRugKDTP8akYhFo5tAA77Qe_NmtuYZc3C3m3I24G2GvR5sSDxUyAN2zq8Lfn9EUms6rY3Ob8YeiKkTiBj0\",\"dq\":\"s9lAH9fggBsoFR8Oac2R_E2gw282rT2kGOAhvIllETE1efrA6huUUvMfBcMpn8lqeW6vzznYY5SSQF7pMdC_agI3nG8Ibp1BUb0JUiraRNqUfLhcQb_d9GF4Dh7e74WbRsobRonujTYN1xCaP6TO61jvWrX-L18txXw494Q_cgk\",\"qi\":\"GyM_p6JrXySiz1toFgKbWV-JdI3jQ4ypu9rbMWx3rQJBfmt0FoYzgUIZEVFEcOqwemRN81zoDAaa-Bk0KWNGDjJHZDdDmFhW3AN7lI-puxk_mHZGJ11rxyR8O55XLSe3SPmRfKwZI6yU24ZxvQKFYItdldUKGzO6Ia6zTKhAVRU\",\"alg\":\"RS256\",\"kid\":\"2099-12-30\"}");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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

//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        return new JdbcRegisteredClientRepository(jdbcTemplate);
//    }

//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("oidc-client")
//                .clientSecret("{noop}secret")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .redirectUri("http://www.baidu.com")
//                .redirectUri("http://localhost:9001/login/oauth2/code/oidc-client")
//                .redirectUri("http://localhost:9001/api/login/welcome")
//                .postLogoutRedirectUri("http://127.0.0.1:8080/")
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//                .scope("message.read")
//                .scope("message.write")
//                .scope("all")
//                // 设置 Client 需要页面审核授权
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//                .build();
//
//        return new InMemoryRegisteredClientRepository(oidcClient);
//    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    private void responseWrite(HttpServletResponse response,ResultData resultData) throws IOException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.OK);
        responseConverter.write(resultData, MediaType.APPLICATION_JSON_UTF8,httpResponse);
    }


    public static void main(String args[]){
//        RSAKey rsaKey = null;
//        try {
//
//            rsaKey = RSAKey.parse("{\"kty\":\"RSA\",\"n\":\"0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx4cbbfAAtVT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMstn64tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FDW2QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n91CbOpbISD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINHaQ-G_xBniIqbw0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw\",\"e\":\"AQAB\",\"d\":\"X4cTteJY_gn4FYPsXB8rdXix5vwsg1FLN5E3EaG6RJoVH-HLLKD9M7dx5oo7GURknchnrRweUkC7hT5fJLM0WbFAKNLWY2vv7B6NqXSzUvxT0_YSfqijwp3RTzlBaCxWp4doFk5N2o8Gy_nHNKroADIkJ46pRUohsXywbReAdYaMwFs9tv8d_cPVY3i07a3t8MN6TNwm0dSawm9v47UiCl3Sk5ZiG7xojPLu4sbg1U2jx4IBTNBznbJSzFHK66jT8bgkuqsk0GjskDJk19Z4qwjwbsnn4j2WBii3RL-Us2lGVkY8fkFzme1z0HbIkfz0Y6mqnOYtqc0X4jfcKoAC8Q\",\"p\":\"83i-7IvMGXoMXCskv73TKr8637FiO7Z27zv8oj6pbWUQyLPQBQxtPVnwD20R-60eTDmD2ujnMt5PoqMrm8RfmNhVWDtjjMmCMjOpSXicFHj7XOuVIYQyqVWlWEh6dN36GVZYk93N8Bc9vY41xy8B9RzzOGVQzXvNEvn7O0nVbfs\",\"q\":\"3dfOR9cuYq-0S-mkFLzgItgMEfFzB2q3hWehMuG0oCuqnb3vobLyumqjVZQO1dIrdwgTnCdpYzBcOfW5r370AFXjiWft_NGEiovonizhKpo9VVS78TzFgxkIdrecRezsZ-1kYd_s1qDbxtkDEgfAITAG9LUnADun4vIcb6yelxk\",\"dp\":\"G4sPXkc6Ya9y8oJW9_ILj4xuppu0lzi_H7VTkS8xj5SdX3coE0oimYwxIi2emTAue0UOa5dpgFGyBJ4c8tQ2VF402XRugKDTP8akYhFo5tAA77Qe_NmtuYZc3C3m3I24G2GvR5sSDxUyAN2zq8Lfn9EUms6rY3Ob8YeiKkTiBj0\",\"dq\":\"s9lAH9fggBsoFR8Oac2R_E2gw282rT2kGOAhvIllETE1efrA6huUUvMfBcMpn8lqeW6vzznYY5SSQF7pMdC_agI3nG8Ibp1BUb0JUiraRNqUfLhcQb_d9GF4Dh7e74WbRsobRonujTYN1xCaP6TO61jvWrX-L18txXw494Q_cgk\",\"qi\":\"GyM_p6JrXySiz1toFgKbWV-JdI3jQ4ypu9rbMWx3rQJBfmt0FoYzgUIZEVFEcOqwemRN81zoDAaa-Bk0KWNGDjJHZDdDmFhW3AN7lI-puxk_mHZGJ11rxyR8O55XLSe3SPmRfKwZI6yU24ZxvQKFYItdldUKGzO6Ia6zTKhAVRU\",\"alg\":\"RS256\",\"kid\":\"2099-12-30\"}");
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//        JWKSet jwkSet = new JWKSet(rsaKey);
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client")
                .clientSecret("{noop}client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("all")
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(24)).build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        System.out.println(client.toString());
    }
}
