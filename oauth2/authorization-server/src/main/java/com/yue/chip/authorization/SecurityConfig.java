package com.yue.chip.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE+1)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.exceptionHandling((exceptions) -> exceptions
                .authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/login"))
        ).authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
        .sessionManagement(sessionManagementConfigurer -> {
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        })
        .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
