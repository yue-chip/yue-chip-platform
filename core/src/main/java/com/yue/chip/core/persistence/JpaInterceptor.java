package com.yue.chip.core.persistence;

import com.yue.chip.core.persistence.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import org.hibernate.Interceptor;
import org.springframework.beans.factory.aspectj.ConfigurableObject;

public class JpaInterceptor implements  Interceptor, ConfigurableObject {


    @PrePersist
    public void setTenantId(BaseEntity baseEntity) {
//        Long tenantId = CurrentUserUtil.getCurrentUserTenantId(false);
//        if (Objects.isNull(tenantId)) {
//            tenantId = 10000L;
//        }
//        if (Objects.nonNull(tenantId) ) {
//            baseEntity.setTenantId(tenantId);
//        }
    }

}
