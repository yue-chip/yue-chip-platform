package com.yue.chip.core.persistence;

import com.yue.chip.utils.TenantSqlUtil;
import lombok.SneakyThrows;
import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * @description: jpa 多租户 拦截器
 * @author: mr.liu
 * @create: 2020-10-20 16:04
 **/
@Deprecated
public class JpaSqlInterceptor implements StatementInspector {

    @SneakyThrows
    @Override
    public String inspect( String sql) {
//        sql = SqlWhereOptimizeUtil.sqlReplace(sql);
//        sql = SqlOrderOptimizeUtil.sqlReplace(sql);
//        sql = TenantSqlUtil.sqlReplace(sql);
        return sql;
    }

}
