package com.yue.chip.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.yue.chip.core.tenant.TenantConstant.PREFIX_TENANT;

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
        log.info("切换数据：".concat(databaseName));
        return databaseName;
    }

    public static String tenantDatabaseName(Long tenantNumber) {
        String databaseName = getPrefixDataBase();
        return tenantDatabaseName(databaseName,tenantNumber);
    }

    public static String getPrefixDataBase(){
        if (!StringUtils.hasText(prefixDatabase)) {
            synchronized (TenantDatabaseUtil.class) {
                String jdbcUrl = ((Environment)SpringContextUtil.getBean(Environment.class)).getProperty("spring.datasource.url");
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

    public static String getDatabaseScript(){
        String databaseScript = "USE ";
        switch (databasePlatform) {
            case "org.hibernate.dialect.MySQL8Dialect" :
                databaseScript = "USE ";
                break;
            case "org.hibernate.dialect.DmDialect" :
                databaseScript = "SET SCHEMA ";
                break;
            default :
                databaseScript = "USE ";
                break;
        }
        return databaseScript;
    }
}
