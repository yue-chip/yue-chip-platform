package com.yue.chip.resource;

import com.yue.chip.security.YueChipAuthenticationEntryPoint;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import com.yue.chip.security.AbstractSecurityConfig;
import com.yue.chip.security.AuthorizationIgnoreConfiguration;

import java.util.List;

@Configuration(proxyBeanMethods = true)
@EnableWebSecurity
@ConditionalOnClass(value = {EnableWebSecurity.class, Servlet.class})
@AutoConfigureAfter(AuthorizationIgnoreConfiguration.class)
@EnableMethodSecurity( prePostEnabled = true, securedEnabled = true, jsr250Enabled = true )
public class ResourceServerConfig extends AbstractSecurityConfig {

    @Value("${jwk.set.uri}")
    private String jwkSetUri;


    public ResourceServerConfig() {
        setRemoveTonkeFilterBeforeClass(BearerTokenAuthenticationFilter.class);
        setAuthorizationToken("authorization");
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity = security(httpSecurity);
        httpSecurity.oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwt -> jwt.decoder(NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()));
        });
        SecurityFilterChain securityFilterChain = httpSecurity.build();
        List<Filter> filterList = securityFilterChain.getFilters();
        filterList.forEach(filter -> {
            if (filter instanceof BearerTokenAuthenticationFilter) {
                BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter = (BearerTokenAuthenticationFilter) filter;
                bearerTokenAuthenticationFilter.setAuthenticationEntryPoint(new YueChipAuthenticationEntryPoint());
            }
        });
        return securityFilterChain;
    }
}
