package com.yue.chip.utils;

import jakarta.validation.constraints.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author coby
 * @description: TODO
 * @date 2024/3/21 上午11:50
 */
public class ConnectionSwitchoverUtil {

    public static void switchoverDatabase(@NotNull Connection conn) {
        if (Objects.isNull(conn)){
            return;
        }
//        Statement statement = null;
//        String tenantDataBaseName = TenantDatabaseUtil.tenantDatabaseName(CurrentUserUtil.getCurrentUserTenantNumber());
//        if (StringUtils.hasText(tenantDataBaseName)) {
//            try {
//                statement = conn.createStatement();
//                statement.execute(TenantDatabaseUtil.getDatabaseScript(tenantDataBaseName));
//            }catch (Exception exception){
//                BusinessException.throwException("切换数据库失败");
//            }finally {
//                HibernateSessionJdbcUtil.close(statement);
//            }
//        }
        try {
            conn.setCatalog(TenantDatabaseUtil.tenantDatabaseName());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
