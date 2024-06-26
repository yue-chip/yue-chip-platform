package com.yue.chip.config;

import com.yue.chip.core.tenant.jdbc.MultiTenantJdbcTemplate;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:52
 */
@Configuration
@ConditionalOnProperty(prefix = "spring",name = "jdbc.multiTenant",havingValue = "enabled")
public class MultiTenantJdbcTemplateConfiguration {

    @Resource
    private DataSource dataSource;

    @Bean
    @Primary
    public MultiTenantJdbcTemplate jdbcTemplate() {
       return new MultiTenantJdbcTemplate(dataSource);
    }

}
