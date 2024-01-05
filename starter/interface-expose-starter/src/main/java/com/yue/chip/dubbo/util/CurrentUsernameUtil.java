package com.yue.chip.dubbo.util;

import com.yue.chip.constant.DubboConstant;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class CurrentUsernameUtil {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private static String getCurrentUser(){
        String username = null;
        if (Objects.isNull(threadLocal.get())) {
            username = com.yue.chip.utils.CurrentUserUtil.getCurrentUserUsername();
            if (StringUtils.hasText(username)){
                threadLocal.set(username);
            }
        }
        if (!StringUtils.hasText(username)) {
            Object obj = RpcContext.getServerContext().getObjectAttachment(DubboConstant.USERNAME);
            if (Objects.nonNull(obj)) {
                username = String.valueOf(obj);
                threadLocal.set(username);
            }
        }
        username = threadLocal.get();

        return username;
    }

    /**
     * 设置当前登录用户
     */
    public static void setCurrentUser(){
        String username = getCurrentUser();
        if (StringUtils.hasText(username)) {
            RpcContext.getServerContext().setObjectAttachment(DubboConstant.USERNAME,username);
        }
    }

    public static void cleanThreadLocal(){
        threadLocal.remove();
    }
}
