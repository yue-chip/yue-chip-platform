package com.yue.chip.config;

import com.yue.chip.core.tenant.jdbc.MultiTenantJdbcTemplate;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:52
 */
//@Configuration
//@ConditionalOnProperty(prefix = "spring",name = "jpa.hibernate.multiTenant",havingValue = "enabled")
public class MultiTenantJdbcTemplateConfiguration {

    @Resource
    private DataSource dataSource;

    @Bean("jdbcTemplate")
    @Primary
    public MultiTenantJdbcTemplate jdbcTemplate() {
       return new MultiTenantJdbcTemplate(dataSource);
    }

}
