package com.yue.chip.core.tenant;

import com.yue.chip.exception.BusinessException;
import com.yue.chip.utils.TenantDatabaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Mr.Liu
 * @description: 多租户链接管理器
 * @date 2023/9/20 上午9:34
 */
@Component
@ConditionalOnProperty(prefix = "spring",name = "jpa.hibernate.multiTenant",havingValue = "enabled")
@Slf4j
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

    @Resource
    private DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        DataSourceUtils.releaseConnection(connection,dataSource);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();
        try {
            connection.createStatement().execute(TenantDatabaseUtil.getDatabaseScript().concat( getTenantDatabaseName()).concat(""));
        }catch (Exception e){
            e.printStackTrace();
            BusinessException.throwException("该租户不存在");
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.createStatement().execute(TenantDatabaseUtil.getDatabaseScript().concat( getTenantDatabaseName()).concat(""));
        DataSourceUtils.releaseConnection(connection,dataSource);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }


    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
        hibernateProperties.put("hibernate.multiTenancy", "schema");
    }

    private String getTenantDatabaseName() {
        return TenantDatabaseUtil.tenantDatabaseName(TenantUtil.getTenantNumber());
    }
}
