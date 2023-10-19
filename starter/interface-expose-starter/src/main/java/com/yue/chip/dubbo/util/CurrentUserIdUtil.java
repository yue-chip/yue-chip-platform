package com.yue.chip.dubbo.util;

import com.yue.chip.constant.DubboConstant;
import org.apache.dubbo.rpc.RpcContext;

import java.util.Objects;

/**
 * @author Mr.Liu
 * @description: TODO
 * @date 2023/9/21 下午5:10
 */
public class CurrentUserIdUtil {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    private static Long getCurrentUserId(){
        Long userId = null;
        if (Objects.isNull(threadLocal.get())) {
            userId = com.yue.chip.utils.CurrentUserUtil.getCurrentUserId(false);
            if (Objects.nonNull(userId)){
                threadLocal.set(userId);
            }
        }
        if (Objects.isNull(userId)) {
            Object obj = RpcContext.getServiceContext().getObjectAttachment(DubboConstant.USER_ID);
            if (Objects.nonNull(obj)) {
                userId = Long.valueOf(String.valueOf(obj));
                threadLocal.set(userId);
            }
        }
        userId = threadLocal.get();
        return userId;
    }

    /**
     * 设置当前登录用户ID
     */
    public static void setCurrentUserId(){
        Long userId = getCurrentUserId();
        if (Objects.nonNull(userId)) {
            RpcContext.getServiceContext().setObjectAttachment(DubboConstant.USER_ID,userId);
        }
    }

    public static void cleanThreadLocal(){
        threadLocal.remove();
    }
}
