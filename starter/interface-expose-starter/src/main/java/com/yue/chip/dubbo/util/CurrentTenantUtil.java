package com.yue.chip.dubbo.util;

import com.yue.chip.constant.DubboConstant;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class CurrentTenantUtil {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    private static Long getCurrentTenant(){
        Long tenantId = null;
        if (Objects.isNull(threadLocal.get())) {
            tenantId = com.yue.chip.utils.CurrentUserUtil.getCurrentUserTenantId(false);
            if (Objects.nonNull(tenantId)){
                threadLocal.set(tenantId);
            }
        }
        if (Objects.isNull(tenantId)) {
            Object obj = RpcContext.getServiceContext().getObjectAttachment(DubboConstant.TENANT_ID);
            if (Objects.nonNull(obj)) {
                tenantId = Long.valueOf(String.valueOf(obj));
                threadLocal.set(tenantId);
            }
        }
        tenantId = threadLocal.get();
        return tenantId;
    }

    /**
     * 设置当前登录用户
     */
    public static void setCurrentTenant(){
        Long tenantId = getCurrentTenant();
        if (Objects.nonNull(tenantId)) {
            RpcContext.getServiceContext().setObjectAttachment(DubboConstant.TENANT_ID,tenantId);
        }
    }

    public static void cleanThreadLocal(){
        threadLocal.remove();
    }
}
