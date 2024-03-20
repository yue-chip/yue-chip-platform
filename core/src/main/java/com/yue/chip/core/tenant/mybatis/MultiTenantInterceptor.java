package com.yue.chip.core.tenant.mybatis;

import com.yue.chip.utils.TenantDatabaseUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author coby
 * @description: TODO
 * @date 2024/3/20 下午2:24
 */
@Component
@ConditionalOnProperty(prefix = "spring",name = "mybatis.multiTenant",havingValue = "enabled")
@Slf4j
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class MultiTenantInterceptor implements Interceptor {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Connection connection = sqlSessionFactory.openSession().getConnection();
        connection.setCatalog(TenantDatabaseUtil.tenantDatabaseName());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
