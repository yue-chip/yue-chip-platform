package com.yue.chip.config;

import com.yue.chip.core.tenant.jpa.MultiTenantConnectionProviderImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:52
 */
@Configuration
@ConditionalOnProperty(prefix = "spring",name = "jpa.hibernate.multiTenant",havingValue = "enabled")
public class MultiTenantJpaConfiguration {

    @Bean
    @Primary
    public MultiTenantConnectionProviderImpl multiTenantConnectionProvider () {
       return new MultiTenantConnectionProviderImpl();
    }

}
