package com.yue.chip.utils;

import com.yue.chip.exception.AuthorizationException;
import com.yue.chip.security.YueChipUserDetails;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @date 2023/6/7 下午3:35
 */
public class YueChipRedisTokenStoreUtil {

    private static volatile RedisTemplate redisTemplate;

    public static void store(YueChipUserDetails yueChipUserDetails,String token) {
        CurrentUserRedisUtil.setUsername(token,yueChipUserDetails.getUsername());
        CurrentUserRedisUtil.setTenantNumber(token,yueChipUserDetails.getTenantNumber());
        CurrentUserRedisUtil.setUserId(token,yueChipUserDetails.getId());
        CurrentUserRedisUtil.setAuthority(token,yueChipUserDetails.getAuthorities());
    }

    public static void clean(String token) {
        CurrentUserRedisUtil.deleteTenantNumber(token);
        CurrentUserRedisUtil.deleteAuthority(token);
        CurrentUserRedisUtil.deleteUserId(token);
        CurrentUserRedisUtil.deleteUsername(token);
    }

    public static void renewal(String token){
        Long i = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long userId =  CurrentUserRedisUtil.getUserId(token);
        if (Objects.isNull(userId)) {
            AuthorizationException.throwException("登陆异常，请重新登陆");
        }
        CurrentUserRedisUtil.expireTenantNumber(token);
        CurrentUserRedisUtil.expireUserId(token);
        CurrentUserRedisUtil.expireUsername(token);
        CurrentUserRedisUtil.expireAuthority(token);
    }

    public static String getUsername(String token) {
        Object obj = getRedisTemplate().opsForValue().get(CurrentUserUtil.TOKEN_USERNAME+token);
        if (Objects.nonNull(obj)) {
            return (String)obj;
        }
        return "";
    }

    private static RedisTemplate getRedisTemplate(){
        if (Objects.isNull(redisTemplate)) {
            synchronized (YueChipRedisTokenStoreUtil.class) {
                redisTemplate = (RedisTemplate) SpringContextUtil.getBean(RedisTemplate.class);
            }
        }
        return redisTemplate;
    }

}
