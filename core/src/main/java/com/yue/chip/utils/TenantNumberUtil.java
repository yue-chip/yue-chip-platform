package com.yue.chip.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mr.Liu
 * @description: TODO
 * @date 2023/9/23 下午5:04
 */
public class TenantNumberUtil {

    public static final String TENANT_REMOTE_HOST = "tenant-remote-host-";

    private static final List<String> uris = Stream.of(new String[]{"/login1","/weixin/login","/weixin/login1","/oauth2/token"}).collect(Collectors.toList());

    public static Long getTenantNumber() {
        Long tenantNumber = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String uri = request.getRequestURI();
            if (Objects.nonNull(request) && uris.contains(uri)) {
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
