package com.yue.chip.utils;

import com.yue.chip.exception.AuthorizationException;
import com.yue.chip.exception.BusinessException;
import com.yue.chip.security.YueChipUserDetails;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Liu
 * @date 2023/6/7 下午3:35
 */
public class YueChipRedisTokenStoreUtil {

    private static volatile RedisTemplate redisTemplate;


    public static void store(YueChipUserDetails yueChipUserDetails,String token) {
        renewal(yueChipUserDetails.getUsername(),yueChipUserDetails.getId(),token);
        CurrentUserRedisUtil.setTenantId(token,yueChipUserDetails.getTenantId());
        CurrentUserRedisUtil.setUserId(token,yueChipUserDetails.getId());
        CurrentUserRedisUtil.setAuthority(token,yueChipUserDetails.getAuthorities());
    }

    public static void renewal(String username,Long userId, String token){
        if (Objects.isNull(userId)) {
            userId =  CurrentUserRedisUtil.getUserId(token);
        }
        if (Objects.isNull(userId)) {
            AuthorizationException.throwException("登陆异常，请重新登陆");
        }
        CurrentUserRedisUtil.setTenantId(token,CurrentUserRedisUtil.getTenantId(token));
        CurrentUserRedisUtil.setUserId(token,userId);
        getRedisTemplate().opsForValue().set(CurrentUserUtil.TOKEN_USERNAME+token,username,30, TimeUnit.MINUTES);
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
