package com.yue.chip.core.tenant;

import com.yue.chip.utils.CurrentUserUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static com.yue.chip.core.tenant.TenantConstant.PREFIX_TENANT;
import static com.yue.chip.core.tenant.TenantConstant.TENANT_ID;

/**
 * @author Mr.Liu
 * @description: 多租户链接管理器
 * @date 2023/9/20 上午9:34
 */
@Component
@ConditionalOnProperty(prefix = "spring",name = "jpa.hibernate.multiTenant",havingValue = "enabled")
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {


    @Value(value = "${spring.datasource.url}")
    private String jdbcUrl;

    @Resource
    private DataSource dataSource;

    private String prefixDatabase;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();
        connection.createStatement().execute("USE " + getTenantDatabaseName());
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.createStatement().execute("USE " + getPrefixDataBase().concat(tenantIdentifier));
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
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

    private String getPrefixDataBase(){
        if (!StringUtils.hasText(prefixDatabase)) {
            synchronized (this) {
                if (StringUtils.hasText(jdbcUrl)) {
                    if (jdbcUrl.indexOf("?")>-1) {
                        prefixDatabase = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1, jdbcUrl.indexOf("?"));
                    }else {
                        prefixDatabase = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1);
                    }
                }
            }
        }
        return prefixDatabase;
    }

    private String getTenantDatabaseName() {
        Long tenantId = CurrentUserUtil.getCurrentUserTenantId(true);
        if (Objects.isNull(tenantId)) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(requestAttributes)) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                if (Objects.nonNull(request)) {
                   Object obj = request.getParameter("tenantId");
                   if (Objects.nonNull(obj) && StringUtils.hasText(String.valueOf(obj))) {
                       tenantId = Long.valueOf(String.valueOf(obj));
                   }
                }
            }
        }
        return getPrefixDataBase().concat(PREFIX_TENANT).concat(Objects.isNull(tenantId)?TENANT_ID:String.valueOf(tenantId));
    }
}
