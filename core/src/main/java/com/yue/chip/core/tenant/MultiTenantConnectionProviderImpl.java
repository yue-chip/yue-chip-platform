package com.yue.chip.core.tenant;

import com.yue.chip.exception.BusinessException;
import com.yue.chip.utils.CurrentUserUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.jdbc.datasource.DataSourceUtils;
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
@Slf4j
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {


    @Value(value = "${spring.datasource.url}")
    private String jdbcUrl;

    @Resource
    private DataSource dataSource;

    private String prefixDatabase;

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
            connection.createStatement().execute("USE " + getTenantDatabaseName());
        }catch (Exception e){
            e.printStackTrace();
            BusinessException.throwException("该租户不存在");
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.createStatement().execute("USE " + getTenantDatabaseName());
        DataSourceUtils.releaseConnection(connection,dataSource);
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
        Long tenantId = TenantUtil.getTenantId();
        String databaseName = "";
        if (Objects.isNull(tenantId)) {
            databaseName = getPrefixDataBase();
        }else {
            databaseName = getPrefixDataBase().concat(PREFIX_TENANT).concat(String.valueOf(tenantId));
        }
//        log.info("切换数据库：".concat(databaseName));
        return databaseName;
    }
}
