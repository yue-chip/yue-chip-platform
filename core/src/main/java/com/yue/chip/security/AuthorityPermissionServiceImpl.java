package com.yue.chip.security;

import com.yue.chip.utils.CurrentUserRedisUtil;
import com.yue.chip.utils.CurrentUserUtil;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @date 2023/6/7 下午3:29
 */
@Component("aps")
@ConditionalOnClass({SecurityConfig.class, SecurityAutoConfiguration.class})
public class AuthorityPermissionServiceImpl implements AuthorityPermission{

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean hasPermission(String... permissions) {
        String username = CurrentUserUtil.getCurrentUserUsername();
        Collection<YueChipSimpleGrantedAuthority> list = CurrentUserRedisUtil.getAuthority();
        for (String code : permissions) {
            YueChipSimpleGrantedAuthority grantedAuthority = new YueChipSimpleGrantedAuthority();
            grantedAuthority.setAuthority(code);
            if (list.contains(grantedAuthority)) {
                return true;
            }
        }
        return false;
    }




}
