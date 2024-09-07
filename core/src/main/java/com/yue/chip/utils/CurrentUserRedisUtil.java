package com.yue.chip.utils;

import com.yue.chip.security.YueChipSimpleGrantedAuthority;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.yue.chip.utils.CurrentUserUtil.TENANT_NUMBER;
import static com.yue.chip.utils.CurrentUserUtil.USER_ID;

/**
 * @author Mr.Liu
 * @description: 当前用户redis操作
 * @date 2023/9/21 下午4:11
 */
public class CurrentUserRedisUtil {

    private static volatile RedisTemplate redisTemplate;

    public static final int timeout = 60*24*365*10;

    public static Long getTenantNumber(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        Object obj = getRedisTemplate().opsForValue().get(TENANT_NUMBER +token);
        if (Objects.nonNull(obj)) {
            return toLong(obj);
        }
        return null;
    }

    public static void setTenantNumber(String token, Long tenantNumber) {
        if (!StringUtils.hasText(token) || Objects.isNull(tenantNumber)) {
            return;
        }
        getRedisTemplate().opsForValue().set(TENANT_NUMBER + token, tenantNumber,timeout, TimeUnit.MINUTES);
    }

    public static void deleteTenantNumber(String token) {
        if (!StringUtils.hasText(token) ) {
            return;
        }
        getRedisTemplate().delete(TENANT_NUMBER + token);
    }

    public static void expireTenantNumber(String token) {
        if (!StringUtils.hasText(token) ) {
            return;
        }
        getRedisTemplate().expire(TENANT_NUMBER + token, timeout, TimeUnit.MINUTES);
    }

    public static Long getUsername(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        Object obj = getRedisTemplate().opsForValue().get(CurrentUserUtil.TOKEN_USERNAME +token);
        if (Objects.nonNull(obj)) {
            return toLong(obj);
        }
        return null;
    }

    public static void setUsername(String token,String username) {
        if (!StringUtils.hasText(token) || !StringUtils.hasText(username)) {
            return;
        }
        getRedisTemplate().opsForValue().set(CurrentUserUtil.TOKEN_USERNAME+token,username,timeout, TimeUnit.MINUTES);
    }

    public static void deleteUsername(String token) {
        if (!StringUtils.hasText(token) ) {
            return;
        }
        getRedisTemplate().delete(CurrentUserUtil.TOKEN_USERNAME + token);
    }

    public static void expireUsername(String token) {
        if (!StringUtils.hasText(token) ) {
            return;
        }
        getRedisTemplate().expire(CurrentUserUtil.TOKEN_USERNAME + token, timeout, TimeUnit.MINUTES);
    }

    public static Long getUserId(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        Object obj = getRedisTemplate().opsForValue().get(USER_ID +token);
        if (Objects.nonNull(obj)) {
            return toLong(obj);
        }
        return null;
    }

    public static void setUserId(String token,Long userId) {
        if (!StringUtils.hasText(token)) {
            return;
        }
        getRedisTemplate().opsForValue().set(USER_ID+token,userId,timeout, TimeUnit.MINUTES);
    }

    public static void deleteUserId(String token) {
        if (!StringUtils.hasText(token) ) {
            return;
        }
        getRedisTemplate().delete(USER_ID + token);
    }

    public static void expireUserId(String token) {
        if (!StringUtils.hasText(token) ) {
            return;
        }
        getRedisTemplate().expire(USER_ID + token, timeout, TimeUnit.MINUTES);
    }

    public static void setAuthority(String token,Collection<GrantedAuthority> authorities) {
        if (!StringUtils.hasText(token)) {
            return;
        }
        getRedisTemplate().opsForValue().set(CurrentUserUtil.AUTHORITY+token,authorities,timeout, TimeUnit.DAYS);
    }

    public static void deleteAuthority(String token) {
        if (!StringUtils.hasText(token) ) {
            return;
        }
        getRedisTemplate().delete(CurrentUserUtil.AUTHORITY + token);
    }

    public static void expireAuthority(String token) {
        if (!StringUtils.hasText(token) ) {
            return;
        }
        getRedisTemplate().expire(CurrentUserUtil.AUTHORITY + token, timeout, TimeUnit.MINUTES);
    }

    public static Collection<YueChipSimpleGrantedAuthority> getAuthority() {
        Object obj = redisTemplate.opsForValue().get(CurrentUserUtil.AUTHORITY+CurrentUserUtil.getToken());
        if (Objects.nonNull(obj)) {
            Collection<YueChipSimpleGrantedAuthority> list = (Collection<YueChipSimpleGrantedAuthority>) obj;
            return list;
        }
        return Collections.EMPTY_LIST;
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
