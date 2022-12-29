package com.yue.chip.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @description: 此类只为解决dubbo序列化问题
 * @author: Mr.Liu
 * @create: 2020-01-24 19:41
 */
@Data
public class YueChipSimpleGrantedAuthority implements GrantedAuthority {

    private String authority;

    @Override
    public String getAuthority() {
        return StringUtils.hasText(authority)?authority:UUID.randomUUID().toString();
    }
}
