package com.yue.chip.config;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 上午10:49
 */
@Configuration
@ConditionalOnClass({RegisteredClientRepository.class})
@ConditionalOnWebApplication
public class OAuth2Configuration {

    @Resource()
    private JdbcOperations jdbcTemplate;

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }
}
