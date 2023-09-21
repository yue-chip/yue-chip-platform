package com.yue.chip.utils;

import com.yue.chip.security.YueChipSimpleGrantedAuthority;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import static com.yue.chip.utils.CurrentUserUtil.TENANT_ID;
import static com.yue.chip.utils.CurrentUserUtil.USER_ID;

/**
 * @author Mr.Liu
 * @description: 当前用户redis操作
 * @date 2023/9/21 下午4:11
 */
public class CurrentUserRedisUtil {

    private static volatile RedisTemplate redisTemplate;

    public static Long getTenantId(String token,String username) {
        Object obj = getRedisTemplate().opsForValue().get(TENANT_ID +token+"_"+  username);
        if (Objects.nonNull(obj)) {
            return toLong(obj);
        }
        return null;
    }

    public static void setTenantId(String token,String username,Long tenantId) {
        getRedisTemplate().opsForValue().set(TENANT_ID + token + "_" + username, tenantId);
    }

    public static Long getUserId(String token,String username) {
        Object obj = getRedisTemplate().opsForValue().get(USER_ID +token+"_"+  username);
        if (Objects.nonNull(obj)) {
            return toLong(obj);
        }
        return null;
    }

    public static void setAuthority(String token,String username,Collection<GrantedAuthority> authorities) {
        getRedisTemplate().opsForValue().set(CurrentUserUtil.AUTHORITY+token+"_"+username,authorities);
    }

    public static Collection<YueChipSimpleGrantedAuthority> getAuthority(String token,String username) {
        Object obj = redisTemplate.opsForValue().get(CurrentUserUtil.AUTHORITY+CurrentUserUtil.getToken()+"_"+username);
        if (Objects.nonNull(obj)) {
            Collection<YueChipSimpleGrantedAuthority> list = (Collection<YueChipSimpleGrantedAuthority>) obj;
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public static void setUserId(String token,String username,Long userId) {
        getRedisTemplate().opsForValue().set(USER_ID+token+"_"+username,userId);
    }

    private static RedisTemplate getRedisTemplate(){
        if(Objects.isNull(redisTemplate)){
            synchronized(CurrentUserUtil.class){
                if(Objects.isNull(redisTemplate)){
                    redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate");
                }
            }
        }
        return redisTemplate;
    }

    private static Long toLong(Object number) {
        if (Objects.nonNull(number)) {
            if (number instanceof Integer) {
                return ((Integer) number).longValue();
            }else if (number instanceof Long) {
                return (Long) number;
            }
        }
        return null;
    }
}
