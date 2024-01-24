package com.yue.chip.core.tenant.jpa;

import com.yue.chip.utils.SpringContextUtil;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static com.yue.chip.core.tenant.jpa.TenantConstant.PREFIX_TENANT;
import static com.yue.chip.core.tenant.jpa.TenantConstant.TENANT_ID;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER;

/**
 * @author Mr.Liu
 * @description: 多租户Resolver
 * @date 2023/9/20 上午9:11
 */
@Component
@ConditionalOnProperty(prefix = "spring",name = "jpa.hibernate.multiTenant",havingValue = "enabled")
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    @Override
    public @UnknownKeyFor @NonNull @Initialized String resolveCurrentTenantIdentifier() {
        if (Objects.nonNull(SpringContextUtil.getApplicationContext())) {
            Long tenantNumber = TenantUtil.getTenantNumber();
            if (Objects.nonNull(tenantNumber)) {
                return PREFIX_TENANT.concat(String.valueOf(tenantNumber));
            }
        }
        return PREFIX_TENANT.concat(TENANT_ID);
    }

    @Override
    public @UnknownKeyFor @NonNull @Initialized boolean validateExistingCurrentSessions() {
        return true;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
