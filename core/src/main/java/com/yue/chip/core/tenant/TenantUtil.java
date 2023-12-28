package com.yue.chip.core.tenant;

import com.yue.chip.exception.BusinessException;
import com.yue.chip.utils.CurrentUserUtil;
import com.yue.chip.utils.SpringContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @description: TODO
 * @date 2023/9/23 下午5:04
 */
public class TenantUtil {

    public static final String TENANT_REMOTE_HOST = "tenant-remote-host-";

    public static Long getTenantNumber() {
        Long tenantNumber = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (Objects.nonNull(request) && (Objects.equals(request.getRequestURI(),"/login1") || Objects.equals(request.getRequestURI(),"/weixin/login"))|| Objects.equals(request.getRequestURI(),"/weixin/login1")) {
                if (Objects.nonNull(request)) {
                    Object obj = request.getParameter("tenantId");
                    if (Objects.isNull(obj)) {
                        obj = request.getParameter("tenantNumber");
                    }
                    if (Objects.nonNull(obj) && StringUtils.hasText(String.valueOf(obj))) {
                        tenantNumber = Long.valueOf(String.valueOf(obj));
                    }else {
                        String remoteHost = request.getHeader("x-forwarded-host");
                        Object objTenantNumber = getRedisTemplate().opsForValue().get(TENANT_REMOTE_HOST.concat(remoteHost));
                        if (Objects.nonNull(objTenantNumber) && StringUtils.hasText(String.valueOf(objTenantNumber))) {
                            tenantNumber = Long.valueOf(String.valueOf(objTenantNumber));
                        }
                        if (Objects.isNull(tenantNumber)){
                            Boolean b = getRedisTemplate().hasKey(TENANT_REMOTE_HOST.concat(remoteHost));
                            if (!b) {
                                throw new AuthenticationServiceException("租户不可用");
                            }
                        }
                    }
                }
            }
        }
        if (Objects.isNull(tenantNumber)) {
            tenantNumber = CurrentUserUtil.getCurrentUserTenantNumber(false);
        }
        return tenantNumber;
    }

    private static RedisTemplate getRedisTemplate() {
        return (RedisTemplate) SpringContextUtil.getBean(RedisTemplate.class);
    }
}
