package com.yue.chip.dubbo.util;

import com.yue.chip.aop.log.SystemLogDataUtil;
import com.yue.chip.constant.DubboConstant;
import org.apache.dubbo.rpc.RpcContext;

import java.util.Objects;

/**
 * 设置调用链id
 */
public class TarceIdUtil {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setTarceId(){
        Long trackId = null;
        if (Objects.isNull(threadLocal.get())) {
            trackId = SystemLogDataUtil.get().getTrackId();
        }
        if (Objects.isNull(trackId)) {
            Object obj = RpcContext.getServiceContext().getObjectAttachments().get(DubboConstant.TRACE_ID);
            if (Objects.nonNull(obj) && obj instanceof Long) {
                trackId = Long.valueOf(String.valueOf(obj));
            }
        }
        if (Objects.isNull(trackId)) {
            trackId = threadLocal.get();
        }
        if (Objects.nonNull(trackId)) {
            threadLocal.set(trackId);
            RpcContext.getServiceContext().setObjectAttachment(DubboConstant.TRACE_ID,trackId);
        }
    }
}
