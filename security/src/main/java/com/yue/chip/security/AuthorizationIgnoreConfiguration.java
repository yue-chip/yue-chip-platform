package com.yue.chip.security;

import cn.hutool.core.util.ReUtil;
import com.yue.chip.annotation.AuthorizationIgnore;
import com.yue.chip.security.properties.AuthorizationIgnoreProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @description: security 排除url鉴权
 * @author: Mr.Liu
 * @create: 2020-02-07 13:31
 */
@Configuration
@ConditionalOnClass( {AuthorizationIgnoreProperties.class} )
@EnableConfigurationProperties(AuthorizationIgnoreProperties.class)
public class AuthorizationIgnoreConfiguration implements InitializingBean {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)}");
    private static final String ASTERISK = "*";

    @Resource
    private WebApplicationContext applicationContext;

    @Resource
    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(mappingInfo -> {
            HandlerMethod handlerMethod = map.get(mappingInfo);
            AuthorizationIgnore authorizationIgnore = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AuthorizationIgnore.class);
            Optional.ofNullable(authorizationIgnore).ifPresent(authIgnore -> mappingInfo.getPathPatternsCondition().getPatternValues().forEach(url -> authorizationIgnoreProperties.getIgnoreUrl().add(ReUtil.replaceAll(url, PATTERN, ASTERISK))));
            AuthorizationIgnore authorizationIgnoreController = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AuthorizationIgnore.class);
            Optional.ofNullable(authorizationIgnoreController).ifPresent(authIgnore -> mappingInfo.getPathPatternsCondition().getPatternValues().forEach(url -> authorizationIgnoreProperties.getIgnoreUrl().add(ReUtil.replaceAll(url, PATTERN, ASTERISK))));
        });
    }
}
