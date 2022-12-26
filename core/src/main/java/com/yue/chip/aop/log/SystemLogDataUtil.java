package com.yue.chip.aop.log;

import com.yue.chip.utils.CurrentUserUtil;

import java.util.Map;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: SystemLogDataUtil
 * @description: 系统日志工具类
 * @date 2020/8/19下午4:38
 */
public class SystemLogDataUtil {
    private static final ThreadLocal<SystemLogData> systemLogDataThreadLocal = new ThreadLocal<SystemLogData>();

    public static SystemLogData get(){
        SystemLogData systemLogData = systemLogDataThreadLocal.get();
        if (Objects.isNull(systemLogData)) {
            systemLogData = systemLogDataThreadLocal.get();
            if (Objects.isNull(systemLogData)) {
                systemLogData = new SystemLogData();
                Map<String,Object> user = CurrentUserUtil.getCurrentUser(false);
                if(Objects.nonNull(user) && user.containsKey("id")){
                    systemLogData.setCurrentUserId(Long.valueOf(String.valueOf(user.get("id"))));
                }
                SystemLogDataUtil.set(systemLogData);
            }
        }
        return systemLogData;
    }

    public static void set(SystemLogData systemLogData){
        systemLogDataThreadLocal.set(systemLogData);
    }



}
