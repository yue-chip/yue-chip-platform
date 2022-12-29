package com.yue.chip.dubbo.util;

import com.yue.chip.constant.DubboConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Objects;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-10 15:05
 **/
public class ClientRemoteAddressUtil {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 获取客户端IP
     * @return
     */
    public static String getClientRemoteAddress(){
        String ip = null;
        if (Objects.isNull(threadLocal.get())){
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(requestAttributes)) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                if (Objects.nonNull(request)) {
                    ip = request.getHeader("requestHost");
                    if (!StringUtils.hasText(ip)) {
                        ip = request.getHeader("X-Real-IP");
                    }
                    if (!StringUtils.hasText(ip)) {
                        ip = request.getRemoteAddr();
                    }
                    threadLocal.set(ip);
                }
            }
        }
        if (!StringUtils.hasText(ip)) {
            Object obj = RpcContext.getServiceContext().getObjectAttachment(DubboConstant.CLIENT_REMOTE_ADDRESS);
            if (Objects.nonNull(obj)) {
                ip = String.valueOf(obj);
                threadLocal.set(ip);
            }
        }
        if (!StringUtils.hasText(ip)){
            ip = threadLocal.get();
        }
        return ip;
    }

    /**
     * 设置客户端ip到调用扩展参数
     */
    public static void setClientRemoteAddress(){
        String ip = getClientRemoteAddress();
        if (StringUtils.hasText(ip)) {
            RpcContext.getServiceContext().setAttachment(DubboConstant.CLIENT_REMOTE_ADDRESS,ip);
        }
    }
}
