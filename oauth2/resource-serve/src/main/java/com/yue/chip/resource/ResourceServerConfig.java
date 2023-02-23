package com.yue.chip.resource;

import com.yue.chip.resource.enums.Scope;
import com.yue.chip.resource.properties.AuthorizationIgnoreProperties;
import com.yue.chip.resource.properties.OauthClientScopeProperties;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Configuration(proxyBeanMethods = true)
@EnableWebSecurity
@ConditionalOnClass(value = {EnableWebSecurity.class, Servlet.class})
@AutoConfigureAfter(AuthorizationIgnoreConfiguration.class)
@EnableMethodSecurity( prePostEnabled = true, securedEnabled = true, jsr250Enabled = true )
public class ResourceServerConfig {
    private static final String AUTHORIZATION = "authorization";
    @Resource
    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    @Resource
    private OauthClientScopeProperties oauthClientScopeProperties;

    @Value("${jwk.set.uri}")
    private String jwkSetUri;

    private YueChipAuthenticationEntryPoint authenticationEntryPoint = new YueChipAuthenticationEntryPoint();

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        authorizationIgnoreProperties.getIgnoreUrl().add("/actuator/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/webjars/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/v3/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-ui*/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-ui.html");
        authorizationIgnoreProperties.getIgnoreUrl().add("/doc.html");
        authorizationIgnoreProperties.getIgnoreUrl().add("/favicon.ico");
        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(authorizationIgnoreProperties.getIgnoreUrl().toArray(new String[]{})).permitAll();
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
        }).exceptionHandling(exception -> {
            exception.authenticationEntryPoint(authenticationEntryPoint);
        }).oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwt -> jwt.decoder(NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()));
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
                                if(StringUtils.hasText(name) && AUTHORIZATION.equals(name.toLowerCase()) ){
                                    return null;
                                }
                                return super.getHeader(name);
                            }
                            @Override
                            public Enumeration<String> getHeaders(String name) {
                                if(StringUtils.hasText(name) && AUTHORIZATION.equals(name.toLowerCase()) ){
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
            }, BearerTokenAuthenticationFilter.class)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        SecurityFilterChain securityFilterChain = httpSecurity.build();
        List<Filter> filterList = securityFilterChain.getFilters();
        filterList.forEach(filter -> {
            if (filter instanceof BearerTokenAuthenticationFilter) {
                BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter = (BearerTokenAuthenticationFilter) filter;
                bearerTokenAuthenticationFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
            }
        });
        return securityFilterChain;
    }
}
