package com.yue.chip.utils;

import com.yue.chip.security.YueChipUserDetails;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Liu
 * @date 2023/6/7 下午3:35
 */
//@Component
public class YueChipRedisTokenStoreUtil {

//    @Resource
    private static volatile RedisTemplate redisTemplate;

    public static void store(YueChipUserDetails yueChipUserDetails,String token) {
        renewal(yueChipUserDetails.getUsername(),yueChipUserDetails.getId(),token);
        getRedisTemplate().opsForValue().set(CurrentUserUtil.USER_ID+yueChipUserDetails.getUsername(),yueChipUserDetails.getId());
        getRedisTemplate().opsForValue().set(CurrentUserUtil.AUTHORITY+yueChipUserDetails.getUsername(),yueChipUserDetails.getAuthorities());
    }

    public static void renewal(String username,Long userId, String token){
        if (Objects.isNull(userId)) {
            Object obj =  getRedisTemplate().opsForValue().get(CurrentUserUtil.USER_ID+username);
            if (Objects.nonNull(obj)) {
                if (obj instanceof Integer) {
                    userId = ((Integer) obj).longValue();
                }else if (obj instanceof Long) {
                    userId = (Long)obj;
                }
            }
        }
        getRedisTemplate().opsForValue().set(CurrentUserUtil.TOKEN_ID+token,userId,30, TimeUnit.MINUTES);
        getRedisTemplate().opsForValue().set(CurrentUserUtil.TOKEN_USERNAME+token,username,30, TimeUnit.MINUTES);
    }

    public static Long getUserId(String token) {
        Object obj = getRedisTemplate().opsForValue().get(CurrentUserUtil.TOKEN_ID+token);
        if (Objects.nonNull(obj)) {
            return (Long)obj;
        }
        return null;
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
