package com.yue.chip.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.yue.chip.core.tenant.mybatis.MultiTenantInterceptor;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author coby
 * @description: TODO
 * @date 2024/3/20 下午5:24
 */
@Configuration
@ConditionalOnProperty(prefix = "spring",name = "mybatis.multiTenant",havingValue = "enabled")
public class MultiTenantMybatisConfiguration {

    @Resource
    private MultiTenantInterceptor multiTenantInterceptor;

    @Bean
    @Primary
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(multiTenantInterceptor);
        return interceptor;
    }
}
