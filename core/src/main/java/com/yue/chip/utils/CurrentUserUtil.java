package com.yue.chip.utils;


import com.yue.chip.constant.DubboConstant;
import com.yue.chip.exception.AuthorizationException;
import com.yue.chip.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 获取当前登陆用户
 * @author: Mr.Liu
 * @create: 2020-02-17 21:48
 */
public class CurrentUserUtil {

    private static volatile CurrentUser currentUser;
    private static final String NAME = "name";
    public static final String AUTHORITY = "authority-";
    public static final String TENANT_ID = "tenant-id-";
    public static final String USER_ID = "user-id-";
    public static final String ID = "id";
    public static final String TOKEN_ID = "token-id-";
    public static final String TOKEN_USERNAME = "token-username-";
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
            user = getCurrentUserBean().findUserToMap(username);
        }
        if((Objects.isNull(user) || user.isEmpty() || user.size() ==0 || !user.containsKey("id")) && isMustLogin){
            AuthorizationException.throwException("登陆异常，请重新登陆");
        }
        return user;
    }

    public static Long getCurrentUserTenantId(Boolean isMustLogin){
        String username = getUsername();
        Long tenantId = null;
        if (isHttpWebRequest()) {
            if (StringUtils.hasText(username)) {
                tenantId = CurrentUserRedisUtil.getTenantId(getToken(),username);
            }
        }else {
            Object obj = RpcContext.getServiceContext().getObjectAttachment(DubboConstant.TENANT_ID);
            if (Objects.nonNull(obj)) {
                tenantId = (Long) obj;
                CurrentUserRedisUtil.setTenantId(username,getToken(),tenantId);
            }
        }
        if (Objects.isNull(tenantId)) {
            Map<String, Object> user = CurrentUserUtil.getCurrentUser(isMustLogin);
            if (Objects.nonNull(user) && user.containsKey(TENANT_ID) && Objects.nonNull(user.containsKey(TENANT_ID))) {
                tenantId = (Long) user.get("tenantId");
                CurrentUserRedisUtil.setTenantId(username,getToken(),tenantId);
            }
        }
        return tenantId;
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
        if(Objects.nonNull(authentication) && authentication instanceof JwtAuthenticationToken){
            Jwt principal = (Jwt) authentication.getPrincipal();
            if (Objects.nonNull(principal)) {
                Object obj = principal.getClaims().get(OAuth2ParameterNames.USERNAME);
                if (Objects.nonNull(obj)) {
                    try {
                        username = new String(Base64.getDecoder().decode((String) obj), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }else if (Objects.nonNull(authentication) && authentication instanceof AbstractAuthenticationToken) {
            username = (String) authentication.getPrincipal();
            if (!StringUtils.hasText(username)) {
                BusinessException.throwException("登录异常");
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
        Long userId = null;
        if (StringUtils.hasText(username)) {
            userId = CurrentUserRedisUtil.getUserId(getToken(),username);
        }

        if (Objects.isNull(userId)) {
            Object obj = RpcContext.getServiceContext().getObjectAttachments().get(DubboConstant.USER_ID);
            if (Objects.nonNull(obj)) {
                userId = (Long) obj;
                CurrentUserRedisUtil.setUserId(getToken(),username,userId);
            }
        }
//        if (Objects.nonNull(userId)) {
//            return userId;
//        }else {
//            Map<String, Object> currentUser = getCurrentUser(isMustLogin);
//            if (Objects.nonNull(currentUser) && currentUser.containsKey(ID)) {
//                userId = (Long) currentUser.get(ID);
//                CurrentUserRedisUtil.setUserId(getToken(), username, userId);
//            }
//        }
        return userId;
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

    public static String getToken(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (Objects.nonNull(request)) {
                Object obj = request.getHeader("token");
                if (Objects.nonNull(obj)) {
                    return String.valueOf(obj);
                }
            }
        }
        return "";
    }



    /**
     * @return
     */
    private static CurrentUser getCurrentUserBean(){
        if(Objects.isNull(currentUser)){
            synchronized(CurrentUserUtil.class){
                if(Objects.isNull(currentUser)){
                    currentUser = (CurrentUser) SpringContextUtil.getBean("currentUser");
                }
            }
        }
        return currentUser;
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