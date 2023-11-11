package com.yue.chip.utils;

import com.yue.chip.core.tenant.TenantUtil;

import java.util.Objects;

import static com.yue.chip.core.tenant.TenantConstant.PREFIX_TENANT;

/**
 * @author Mr.Liu
 * @description: TODO
 * @date 2023/11/11 上午10:16
 */
public class TenantDatabaseUtil {

    public static String tenantDatabaseName(String dataBaseName,Long tenantNumber) {
        String databaseName = "";
        if (Objects.isNull(tenantNumber)) {
            databaseName = dataBaseName;
        }else {
            databaseName = dataBaseName.concat(PREFIX_TENANT).concat(String.valueOf(tenantNumber));
        }
        return databaseName;
    }
}
