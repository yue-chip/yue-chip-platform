package com.yue.chip.core.persistence;

import com.yue.chip.core.persistence.entity.BaseEntity;
import com.yue.chip.utils.CurrentUserUtil;
import com.yue.chip.utils.TenantSqlUtil;
import lombok.SneakyThrows;
import org.hibernate.Interceptor;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.hibernate.type.Type;
import java.util.Objects;

/**
 * @description: jpa 多租户 拦截器
 * @author: mr.liu
 * @create: 2020-10-20 16:04
 **/
public class JpaTenantInterceptor implements Interceptor,StatementInspector {

    @SneakyThrows
    @Override
    public String inspect( String sql) {
        return TenantSqlUtil.sqlReplace(sql);
    }
}
