package com.yue.chip.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.yue.chip.core.tenant.jpa.TenantConstant.PREFIX_TENANT;

/**
 * @author Mr.Liu
 * @description: TODO
 * @date 2023/11/11 上午10:16
 */
public class TenantDatabaseUtil {
    private static Logger log = LoggerFactory.getLogger(TenantDatabaseUtil.class);
    private static String prefixDatabase;

    private static String databasePlatform;

    public static String tenantDatabaseName(String dataBaseName,Long tenantNumber) {
        String databaseName = "";
        if (Objects.isNull(tenantNumber)) {
            databaseName = dataBaseName;
        }else {
            databaseName = dataBaseName.concat(PREFIX_TENANT).concat(String.valueOf(tenantNumber));
        }
//        log.info("切换数据：".concat(databaseName));
        return databaseName;
    }

    public static String tenantDatabaseName(Long tenantNumber) {
        String databaseName = getPrefixDataBase();
        return tenantDatabaseName(databaseName,tenantNumber);
    }

    public static String tenantDatabaseName() {
        return tenantDatabaseName(CurrentUserUtil.getCurrentUserTenantNumber());
    }

    public static String getPrefixDataBase(){
        if (!StringUtils.hasText(prefixDatabase)) {
            synchronized (TenantDatabaseUtil.class) {
                Environment environment = ((Environment)SpringContextUtil.getBean(Environment.class));
                if (Objects.isNull(environment)) {
                    return "";
                }
                String jdbcUrl = environment.getProperty("spring.datasource.url");
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

    private static String getDatabasePlatform(){
        if (StringUtils.hasText(databasePlatform)) {
            return databasePlatform;
        }
        synchronized (TenantDatabaseUtil.class) {
            if (!StringUtils.hasText(databasePlatform)) {
                databasePlatform = ((Environment) SpringContextUtil.getBean(Environment.class)).getProperty("spring.jpa.database-platform");
                if (!StringUtils.hasText(databasePlatform)) {
                    databasePlatform = "org.hibernate.dialect.MySQL8Dialect";
                }
            }
        }
        return databasePlatform;
    }

    public static String getDatabaseScript(String tenantDataBaseName){
        getDatabasePlatform();
        String databaseScript = "USE `".concat(tenantDataBaseName).concat("`");
        switch (databasePlatform) {
            case "org.hibernate.dialect.MySQL8Dialect" :
                databaseScript = "USE `".concat(tenantDataBaseName).concat("`");
                break;
            case "org.hibernate.dialect.DmDialect" :
                databaseScript = "SET SCHEMA ".concat(tenantDataBaseName);
                break;
            default :
                databaseScript = "USE `".concat(tenantDataBaseName).concat("`");
                break;
        }
        return databaseScript;
    }

    public static String getDatabaseScript(){
        return getDatabaseScript(tenantDatabaseName(CurrentUserUtil.getCurrentUserTenantNumber()));
    }
}
