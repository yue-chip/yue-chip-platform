package com.yue.chip.security;

import com.yue.chip.security.properties.OauthClientScopeProperties;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Setter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import com.yue.chip.security.properties.AuthorizationIgnoreProperties;

import java.io.IOException;
import java.util.Enumeration;
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
        httpSecurity.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(authorizationIgnoreProperties.getIgnoreUrl().toArray(new String[]{})).permitAll();
                }).exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(authenticationEntryPoint);
                })
//        .authorizeHttpRequests(authorize -> {
//            authorize.requestMatchers(HttpMethod.GET).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+ Scope.READ.getName().toLowerCase()+"')" ))
//                    .requestMatchers(HttpMethod.POST).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.WRITE.getName().toLowerCase()+"')"))
//                    .requestMatchers(HttpMethod.PUT).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')"))
//                    .requestMatchers(HttpMethod.DELETE).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.DELETE.getName().toLowerCase()+"')"))
//                    .requestMatchers(HttpMethod.PATCH).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')"));
//        })
                .authorizeHttpRequests(authorize ->{
                    authorize.anyRequest().authenticated();
                }).addFilterBefore(new Filter() {
                    @Override
                    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                        String uri = httpServletRequest.getRequestURI();
                        if (authorizationIgnoreProperties.getIgnoreUrl().contains("/**") || authorizationIgnoreProperties.getIgnoreUrl().contains(uri)){
                            //假装从header删除Authorization
                            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
                                @Override
                                public String getHeader(String name) {
                                    if(StringUtils.hasText(name) && authorizationToken.equals(name.toLowerCase()) ){
                                        return null;
                                    }
                                    return super.getHeader(name);
                                }
                                @Override
                                public Enumeration<String> getHeaders(String name) {
                                    if(StringUtils.hasText(name) && authorizationToken.equals(name.toLowerCase()) ){
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
                }, Objects.nonNull(removeTonkeFilterBeforeClass)? removeTonkeFilterBeforeClass : BasicAuthenticationFilter.class)
                .sessionManagement(sessionManagementConfigurer -> {
                    sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });
        return httpSecurity;
    }
}