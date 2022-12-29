package com.yue.chip.resource;

import com.yue.chip.core.IResultData;
import com.yue.chip.core.ResultData;
import com.yue.chip.core.YueChipObjectMapper;
import com.yue.chip.core.common.enums.ResultDataState;
import com.yue.chip.resource.enums.Scope;
import com.yue.chip.resource.properties.AuthorizationIgnoreProperties;
import com.yue.chip.resource.properties.OauthClientScopeProperties;
import jakarta.annotation.Resource;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@ConditionalOnClass(value = {EnableWebSecurity.class, Servlet.class})
@EnableGlobalAuthentication()
@AutoConfigureAfter(AuthorizationIgnoreConfiguration.class)
public class ResourceServerConfig {

    @Resource
    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    @Resource
    private OauthClientScopeProperties oauthClientScopeProperties;

    @Autowired
    private YueChipObjectMapper yueChipObjectMapper;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        authorizationIgnoreProperties.getIgnoreUrl().add("/actuator/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/webjars/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/resources/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-resources/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/v2/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/v3/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-ui/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/favicon.ico");
        authorizationIgnoreProperties.getIgnoreUrl().add("/oauth/**");

        httpSecurity.authorizeHttpRequests().requestMatchers(authorizationIgnoreProperties.getIgnoreUrl().toArray(new String[]{})).permitAll();
        if (Objects.nonNull(oauthClientScopeProperties.getScopes()) && oauthClientScopeProperties.getScopes().size()>0){
            oauthClientScopeProperties.getScopes().forEach(scopes -> {
                try {
                    httpSecurity.authorizeHttpRequests().requestMatchers(scopes.getUrl().toArray(new String[]{})).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+scopes.getScope()+"')"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+ Scope.READ.getName().toLowerCase()+"')" ))
                .requestMatchers(HttpMethod.POST).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.WRITE.getName().toLowerCase()+"')"))
                .requestMatchers(HttpMethod.PUT).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')"))
                .requestMatchers(HttpMethod.DELETE).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.DELETE.getName().toLowerCase()+"')"))
                .requestMatchers(HttpMethod.PATCH).access( new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')"))
        .and()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
        .and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.setCharacterEncoding("UTF-8");
                        response.setStatus(HttpStatus.OK.value());
                        response.setContentType("application/json");
                        ResultData resultData = ResultData.failed(ResultDataState.NO_PERMISSION.getKey(),"鉴别异常！请重新登陆/联系管理员");
                        PrintWriter printWriter = response.getWriter();
                        printWriter.println(yueChipObjectMapper.writeValueAsString(resultData));
                        printWriter.flush();
                        printWriter.close();
                    }
                });
//        httpSecurity.oauth2ResourceServer().authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
        return httpSecurity.build();
    }
}
