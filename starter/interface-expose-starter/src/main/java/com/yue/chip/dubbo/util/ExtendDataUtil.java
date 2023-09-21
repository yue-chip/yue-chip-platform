package com.yue.chip.dubbo.util;

public class ExtendDataUtil {
    public static void setExtendData(){
        ClientRemoteAddressUtil.setClientRemoteAddress();
        CurrentUsernameUtil.setCurrentUser();
        CurrentTenantUtil.setCurrentTenant();
        CurrentUserIdUtil.setCurrentUserId();
    }

    public static void cleanThreadLocal(){
        CurrentUsernameUtil.cleanThreadLocal();
        ClientRemoteAddressUtil.cleanThreadLocal();
        CurrentTenantUtil.cleanThreadLocal();
        CurrentUserIdUtil.cleanThreadLocal();
    }
}
