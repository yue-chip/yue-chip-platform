package com.yue.chip.core.tenant.jpa;

import com.yue.chip.utils.ConnectionSwitchoverUtil;
import jakarta.annotation.Resource;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Mr.Liu
 * @description: 多租户链接管理器
 * @date 2023/9/20 上午9:34
 */

public class MultiTenantConnectionProviderImpl extends AbstractMultiTenantConnectionProvider implements HibernatePropertiesCustomizer {

    @Resource
    private DataSource dataSource;

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return null;
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(Object tenantIdentifier) {
        return null;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
        hibernateProperties.put("hibernate.multi_tenancy", "schema");
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        ConnectionSwitchoverUtil.switchoverDatabase(connection);
        return connection;
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        return getAnyConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        DataSourceUtils.releaseConnection(connection,dataSource);
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        releaseAnyConnection( connection);
    }
}
