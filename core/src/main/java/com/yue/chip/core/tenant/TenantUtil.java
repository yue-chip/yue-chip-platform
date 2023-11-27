package com.yue.chip.core.tenant;

import com.yue.chip.utils.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author Mr.Liu
 * @description: TODO
 * @date 2023/9/23 下午5:04
 */
public class TenantUtil {

    public static Long getTenantId() {
        Long tenantId = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//            if (Objects.nonNull(request) && Objects.equals(request.getRequestURI(),"/login1")) {
//                String remoteHost = request.getRemoteHost();

                if (Objects.nonNull(request)) {
                    Object obj = request.getParameter("tenantId");
                    if (Objects.nonNull(obj) && StringUtils.hasText(String.valueOf(obj))) {
                        tenantId = Long.valueOf(String.valueOf(obj));
                    }
//                }
            }
        }
        if (Objects.isNull(tenantId)) {
            tenantId = CurrentUserUtil.getCurrentUserTenantId(false);
        }
        return tenantId;
    }
}
