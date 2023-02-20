package com.yue.chip.dubbo.util;

public class ExtendDataUtil {
    public static void setExtendData(){
        ClientRemoteAddressUtil.setClientRemoteAddress();
        CurrentUserUtil.setCurrentUser();
//        TarceIdUtil.setTarceId();
    }

    public static void cleanThreadLocal(){
        CurrentUserUtil.cleanThreadLocal();
        ClientRemoteAddressUtil.cleanThreadLocal();
    }
}
