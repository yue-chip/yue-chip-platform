package com.yue.chip.utils;


import com.yue.chip.constant.DubboConstant;
import com.yue.chip.core.ICurrentUser;
import com.yue.chip.exception.AuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.module.FindException;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 获取当前登陆用户
 * @author: Mr.Liu
 * @create: 2020-02-17 21:48
 */
public class CurrentUserUtil {

    private static volatile ICurrentUser iCurrentUser;
    private static final String NAME = "name";
    private static final String TENANT_ID = "tenantId";
    private static final String USER_ID = "userId";
    private static final String ID = "id";
    private static volatile RedisTemplate redisTemplate;

    public static Map<String,Object> getCurrentUser(){
        return getCurrentUser(true);
    }
    /**
     * 获取当前登陆用户
     * @return
     */
    public static Map<String,Object> getCurrentUser(Boolean isMustLogin){
        Map<String,Object> user = null;
        String username = null;
        if(isHttpWebRequest()){
            username = getUsername();
        }else {
            RpcContext rpcContext = RpcContext.getServiceContext();
            username = String.valueOf(rpcContext.getObjectAttachments().get(DubboConstant.USERNAME));
        }
        if(StringUtils.hasText(username)) {
            user = getICurrentUser().findUserToMap(username);
        }
        if((Objects.isNull(user) || user.isEmpty() || user.size() ==0 || !user.containsKey("id")) && isMustLogin){
            AuthorizationException.throwException("登陆异常，请重新登陆");
        }
        return user;
    }

    public static Long getCurrentUserTenantId(Boolean isMustLogin){
        String username = getUsername();
        Object tenantId = null;
        if (StringUtils.hasText(username)) {
            tenantId = getRedisTemplate().opsForValue().get(TENANT_ID + "-" + username);
            if (Objects.nonNull(tenantId)) {
                return (Long) tenantId;
            }
        }else {
            return null;
        }
        Map<String,Object> user = CurrentUserUtil.getCurrentUser(isMustLogin);
        if (Objects.nonNull(user) && user.containsKey(TENANT_ID) && Objects.nonNull(user.containsKey(TENANT_ID))) {
            tenantId = user.get(TENANT_ID);
            getRedisTemplate().opsForValue().set(TENANT_ID+"-"+username,(Long)tenantId);
            return (Long)tenantId;
        }
        return null;
    }

    public static Long getCurrentUserTenantId(){
        return getCurrentUserTenantId(true);
    }

    /**
     * 获取当前用户姓名
     * @return
     */
    private static String getCurrentUserName(){
        Map<String,Object> currentUser = getCurrentUser();
        if(currentUser.containsKey(NAME)){
            return String.valueOf(currentUser.get(NAME));
        }
        return "";
    }

    /**
     * 获取当前登陆用户登陆账号
     * @return
     */
    public static String getCurrentUserUsername(){
        if(isHttpWebRequest()){
            return getUsername();
        }else{
            Object obj = RpcContext.getServiceContext().getObjectAttachments().get(DubboConstant.USERNAME);
            if (Objects.nonNull(obj)) {
                String username = String.valueOf(obj);
                return username;
            }
        }
        return "";
    }

    /**
     * 获取token username
     * @return
     */
    private static String getUsername(){
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(authentication)){
            Object principal = authentication.getPrincipal();
            if(Objects.nonNull(principal) && principal instanceof String){
                username = String.valueOf(principal);
            }
        }
        return username;
    }

    /**
     * 获取当前用户ID
     * @return
     */
    public static Long getCurrentUserId(){
        return getCurrentUserId(true);
    }

    public static Long getCurrentUserId(Boolean isMustLogin){
        String username = getUsername();
        Object userId = null;
        if (StringUtils.hasText(username)) {
            userId = getRedisTemplate().opsForValue().get(USER_ID + "-" + username);
            if (Objects.nonNull(userId)) {
                return (Long) userId;
            }
        }else {
            return null;
        }
        Map<String,Object> currentUser = getCurrentUser(isMustLogin);
        if(Objects.nonNull(currentUser) && currentUser.containsKey(ID)){
            userId = currentUser.get(ID);
            getRedisTemplate().opsForValue().set(USER_ID+"-"+username,(Long)userId);
            return (Long)userId;
        }
        return null;
    }

    /**
     * 判断是不是http请求
     * @return
     */
    private static boolean isHttpWebRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (Objects.nonNull(request)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    private static ICurrentUser getICurrentUser(){
        if(Objects.isNull(iCurrentUser)){
            synchronized(CurrentUserUtil.class){
                if(Objects.isNull(iCurrentUser)){
                    iCurrentUser = (ICurrentUser) SpringContextUtil.getBean("currentUser");
                }
            }
        }
        return iCurrentUser;
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

}
