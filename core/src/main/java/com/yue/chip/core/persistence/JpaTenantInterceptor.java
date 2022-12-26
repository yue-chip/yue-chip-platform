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

    private static final String TENANT_ID ="tenant_id";

    @SneakyThrows
    @Override
    public String inspect( String sql) {
        return TenantSqlUtil.sqlReplace(sql);
    }

    @Override
    public boolean onSave(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) {
        Long tenantId = CurrentUserUtil.getCurrentUserTenantId(false);
        BaseEntity baseEntity = (BaseEntity)entity;
        if (Objects.isNull(baseEntity.getId()) && Objects.nonNull(tenantId) ) {
            baseEntity.setTenantId(tenantId);
        }
        return false;
    }
}
