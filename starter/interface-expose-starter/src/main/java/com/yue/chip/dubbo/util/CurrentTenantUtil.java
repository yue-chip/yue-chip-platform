package com.yue.chip.dubbo.util;

import com.yue.chip.constant.DubboConstant;
import org.apache.dubbo.rpc.RpcContext;

import java.util.Objects;

public class CurrentTenantUtil {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    private static Long getCurrentTenant(){
        Long tenantNumber = null;
        if (Objects.isNull(threadLocal.get())) {
            tenantNumber = com.yue.chip.utils.CurrentUserUtil.getCurrentUserTenantNumber(false);
            if (Objects.nonNull(tenantNumber)){
                threadLocal.set(tenantNumber);
            }
        }
        if (Objects.isNull(tenantNumber)) {
            Object obj = RpcContext.getServerContext().getObjectAttachment(DubboConstant.TENANT_NUMBER);
            if (Objects.nonNull(obj)) {
                tenantNumber = Long.valueOf(String.valueOf(obj));
                threadLocal.set(tenantNumber);
            }
        }
        tenantNumber = threadLocal.get();
        return tenantNumber;
    }

    /**
     * 设置当前登录用户
     */
    public static void setCurrentTenant(){
        Long tenantNumber = getCurrentTenant();
        if (Objects.nonNull(tenantNumber)) {
            RpcContext.getServerContext().setObjectAttachment(DubboConstant.TENANT_NUMBER,tenantNumber);
        }
    }

    public static void cleanThreadLocal(){
        threadLocal.remove();
    }
}
