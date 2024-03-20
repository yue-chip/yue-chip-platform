package com.yue.chip.config;

import com.yue.chip.core.tenant.mybatis.MultiTenantInterceptor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author coby
 * @description: TODO
 * @date 2024/3/20 下午5:24
 */
@Configuration
@ConditionalOnProperty(prefix = "spring",name = "mybatis.multiTenant",havingValue = "enabled")
public class MybatisMultiTenantConfig {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private MultiTenantInterceptor multiTenantInterceptor;

    @PostConstruct
    public void addInterceptor(){
        sqlSessionFactory.getConfiguration().addInterceptor(multiTenantInterceptor);
    }
}
