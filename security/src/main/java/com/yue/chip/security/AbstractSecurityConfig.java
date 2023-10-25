package com.yue.chip.security;

import com.yue.chip.security.filter.YueChipAuthenticationFilter;
import com.yue.chip.security.properties.OauthClientScopeProperties;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import com.yue.chip.security.properties.AuthorizationIgnoreProperties;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @date 2023/5/25 上午10:46
 */
public abstract class AbstractSecurityConfig {

    @Setter
    private String authorizationToken = "authorization";

    @Setter
    private Class<? extends Filter> removeTonkeFilterBeforeClass;

    @Resource
    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    @Resource
    private OauthClientScopeProperties oauthClientScopeProperties;

    private YueChipAuthenticationEntryPoint authenticationEntryPoint = new YueChipAuthenticationEntryPoint();

    protected HttpSecurity security(HttpSecurity httpSecurity) throws Exception {
        authorizationIgnoreProperties.getIgnoreUrl().add("/actuator/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/webjars/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/v3/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-ui*/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-ui.html");
        authorizationIgnoreProperties.getIgnoreUrl().add("/doc.html");
        authorizationIgnoreProperties.getIgnoreUrl().add("/favicon.ico");
        YueChipAuthenticationFilter yueChipAuthenticationFilter = new YueChipAuthenticationFilter();
        yueChipAuthenticationFilter.setAuthorizationIgnoreProperties(authorizationIgnoreProperties);
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> {
                    httpSecurityCsrfConfigurer.disable();
                }).cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.disable();
                }).authorizeHttpRequests(authorize -> {
                    String[] urls = authorizationIgnoreProperties.getIgnoreUrl().toArray(new String[]{});
                    authorize.requestMatchers(urls).permitAll().anyRequest().fullyAuthenticated();
                }).exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(authenticationEntryPoint);
                })
                .addFilterBefore(new Filter() {
                    @Override
                    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                        String uri = httpServletRequest.getRequestURI();
                        if (authorizationIgnoreProperties.getIgnoreUrl().contains("/**") || authorizationIgnoreProperties.getIgnoreUrl().contains(uri)){
                            //假装从header删除Authorization
                            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
                                @Override
                                public String getHeader(String name) {
                                    if(StringUtils.hasText(name) && (authorizationToken.equals(name.toLowerCase())  || Objects.equals("token",name.toLowerCase()))){
                                        return null;
                                    }
                                    return super.getHeader(name);
                                }
                                @Override
                                public Enumeration<String> getHeaders(String name) {
                                    if(StringUtils.hasText(name) && (authorizationToken.equals(name.toLowerCase())  || Objects.equals("token",name.toLowerCase()))){
                                        return new Enumeration() {
                                            @Override
                                            public boolean hasMoreElements() {
                                                return false;
                                            }
                                            @Override
                                            public Object nextElement() {
                                                return null;
                                            }
                                        };
                                    }
                                    return super.getHeaders(name);
                                }
                            };
                            chain.doFilter(requestWrapper,response );
                        }else {
                            chain.doFilter(request, response);
                        }
                    }
                }, Objects.nonNull(removeTonkeFilterBeforeClass)? removeTonkeFilterBeforeClass : AnonymousAuthenticationFilter.class)
                .addFilterBefore(yueChipAuthenticationFilter, AuthorizationFilter.class)
                .sessionManagement(sessionManagementConfigurer -> {
                    sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });
        return httpSecurity;
    }
}