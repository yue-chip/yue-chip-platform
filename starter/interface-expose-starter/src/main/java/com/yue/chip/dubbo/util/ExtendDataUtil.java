package com.yue.chip.dubbo.util;

public class ExtendDataUtil {
    public static void setExtendData(){
        ClientRemoteAddressUtil.setClientRemoteAddress();
        CurrentUserUtil.setCurrentUser();
    }

    public static void cleanThreadLocal(){
        CurrentUserUtil.cleanThreadLocal();
        ClientRemoteAddressUtil.cleanThreadLocal();
    }
}
