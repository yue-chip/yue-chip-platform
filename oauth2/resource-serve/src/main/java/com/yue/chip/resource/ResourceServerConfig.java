package com.yue.chip.resource;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.yue.chip.core.ResultData;
import com.yue.chip.core.YueChipObjectMapper;
import com.yue.chip.core.common.enums.ResultDataState;
import com.yue.chip.resource.enums.Scope;
import com.yue.chip.resource.properties.AuthorizationIgnoreProperties;
import com.yue.chip.resource.properties.OauthClientScopeProperties;
import jakarta.annotation.Resource;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@ConditionalOnClass(value = {EnableWebSecurity.class, Servlet.class})
@AutoConfigureAfter(AuthorizationIgnoreConfiguration.class)
@EnableMethodSecurity( prePostEnabled = true, securedEnabled = true, jsr250Enabled = true )
public class ResourceServerConfig {

    @Resource
    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    @Resource
    private OauthClientScopeProperties oauthClientScopeProperties;

    @Autowired
    private YueChipObjectMapper yueChipObjectMapper;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        authorizationIgnoreProperties.getIgnoreUrl().add("/actuator/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/webjars/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/resources/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-resources/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/v2/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/v3/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-ui/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/favicon.ico");
        authorizationIgnoreProperties.getIgnoreUrl().add("/oauth/**");

        httpSecurity.authorizeHttpRequests().requestMatchers(authorizationIgnoreProperties.getIgnoreUrl().toArray(new String[]{})).permitAll();
        if (Objects.nonNull(oauthClientScopeProperties.getScopes()) && oauthClientScopeProperties.getScopes().size()>0){
            oauthClientScopeProperties.getScopes().forEach(scopes -> {
                try {
                    httpSecurity.authorizeHttpRequests().requestMatchers(scopes.getUrl().toArray(new String[]{})).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+scopes.getScope()+"')"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+ Scope.READ.getName().toLowerCase()+"')" ))
                .requestMatchers(HttpMethod.POST).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.WRITE.getName().toLowerCase()+"')"))
                .requestMatchers(HttpMethod.PUT).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')"))
                .requestMatchers(HttpMethod.DELETE).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.DELETE.getName().toLowerCase()+"')"))
                .requestMatchers(HttpMethod.PATCH).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')"))
        .and()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
        .and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.setCharacterEncoding("UTF-8");
                        response.setStatus(HttpStatus.OK.value());
                        response.setContentType("application/json");
                        ResultData resultData = ResultData.failed(ResultDataState.NO_PERMISSION.getKey(),"鉴权异常！请重新登陆/联系管理员");
                        PrintWriter printWriter = response.getWriter();
                        printWriter.println(yueChipObjectMapper.writeValueAsString(resultData));
                        printWriter.flush();
                        printWriter.close();
                    }
                })
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return httpSecurity.build();
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
}
