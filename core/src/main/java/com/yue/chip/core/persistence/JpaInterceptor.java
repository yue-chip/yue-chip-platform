package com.yue.chip.core.persistence;

import com.yue.chip.core.persistence.entity.BaseEntity;
import com.yue.chip.utils.CurrentUserUtil;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.aspectj.ConfigurableObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Configurable
public class JpaInterceptor implements ConfigurableObject {


    @PrePersist
    public void setTenantId(BaseEntity baseEntity) {
        Long tenantId = CurrentUserUtil.getCurrentUserTenantId(false);
        if (Objects.isNull(tenantId)) {
            tenantId = 10000L;
        }
        if (Objects.nonNull(tenantId) ) {
            baseEntity.setTenantId(tenantId);
        }
    }
}
