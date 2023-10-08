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
//@Component
public class YueChipRedisTokenStoreUtil {

//    @Resource
    private static volatile RedisTemplate redisTemplate;

    private static volatile UserDetailsService userDetailsService;

    public static void store(YueChipUserDetails yueChipUserDetails,String token) {
        renewal(yueChipUserDetails.getUsername(),yueChipUserDetails.getId(),token);
        CurrentUserRedisUtil.setTenantId(token,yueChipUserDetails.getUsername(),yueChipUserDetails.getTenantId());
        CurrentUserRedisUtil.setUserId(token,yueChipUserDetails.getUsername(),yueChipUserDetails.getId());
        CurrentUserRedisUtil.setAuthority(token,yueChipUserDetails.getUsername(),yueChipUserDetails.getAuthorities());

    }

    public static void renewal(String username,Long userId, String token){
        if (Objects.isNull(userId)) {
            userId =  CurrentUserRedisUtil.getUserId(token,username);
        }
        if (Objects.isNull(userId)) {
            AuthorizationException.throwException("登陆异常，请重新登陆");
        }
//        if (Objects.nonNull(userId)) {
//            YueChipUserDetails userDetails = (YueChipUserDetails) getUserDetailsService().loadUserByUsername(username);
//            if (Objects.nonNull(userDetails)) {
//                getRedisTemplate().opsForValue().set(CurrentUserUtil.TOKEN_ID + token, userDetails.getId(), 30, TimeUnit.MINUTES);
//            }
//        }
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

    private static UserDetailsService getUserDetailsService(){
        if (Objects.isNull(userDetailsService)) {
            synchronized (UserDetailsService.class) {
                userDetailsService = (UserDetailsService) SpringContextUtil.getBean(UserDetailsService.class);
            }
        }
        return userDetailsService;
    }

}
