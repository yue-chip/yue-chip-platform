package com.yue.chip.security;

import com.yue.chip.utils.CurrentUserUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @date 2023/6/7 下午3:29
 */
@Service("aps")
public class AuthorityPermissionServiceImpl implements AuthorityPermission{

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean hasPermission(String... permissions) {
        String username = CurrentUserUtil.getCurrentUserUsername();
        Object obj = redisTemplate.opsForValue().get(CurrentUserUtil.AUTHORITY+username);
        if (Objects.nonNull(obj)) {
            Collection<YueChipSimpleGrantedAuthority> list = (Collection<YueChipSimpleGrantedAuthority>) obj;
            for (String code : permissions) {
                YueChipSimpleGrantedAuthority grantedAuthority = new YueChipSimpleGrantedAuthority();
                grantedAuthority.setAuthority(code);
                if (list.contains(grantedAuthority)) {
                    return true;
                }
            }
        }
        return false;
    }


}
