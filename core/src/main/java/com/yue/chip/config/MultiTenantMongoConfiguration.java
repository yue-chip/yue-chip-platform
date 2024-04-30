package com.yue.chip.config;

import com.yue.chip.core.tenant.mongo.MultiTenantMongoDbFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * @author coby
 * @description: TODO
 * @date 2024/3/20 下午5:24
 */
@Configuration
@ConditionalOnProperty(prefix = "spring",name = "data.mongodb.multiTenant",havingValue = "enabled")
public class MultiTenantMongoConfiguration {

    @Bean
    @Primary
    public SimpleMongoClientDatabaseFactory mongoDatabaseFactory() {
        return new MultiTenantMongoDbFactory();
    }
}
