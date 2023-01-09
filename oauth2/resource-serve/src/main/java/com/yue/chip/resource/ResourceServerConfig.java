package com.yue.chip.resource;

import com.yue.chip.core.ResultData;
import com.yue.chip.core.YueChipObjectMapper;
import com.yue.chip.core.common.enums.ResultDataState;
import com.yue.chip.resource.enums.Scope;
import com.yue.chip.resource.properties.AuthorizationIgnoreProperties;
import com.yue.chip.resource.properties.OauthClientScopeProperties;
import jakarta.annotation.Resource;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration(proxyBeanMethods = true)
@EnableWebSecurity
@ConditionalOnClass(value = {EnableWebSecurity.class, Servlet.class})
@AutoConfigureAfter(AuthorizationIgnoreConfiguration.class)
@EnableMethodSecurity( prePostEnabled = true, securedEnabled = true, jsr250Enabled = true )
public class ResourceServerConfig {

    @Resource
    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    @Resource
    private OauthClientScopeProperties oauthClientScopeProperties;

    @Autowired
    private YueChipObjectMapper yueChipObjectMapper;

    @Value("${jwk.set.uri}")
    private String jwkSetUri;

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
        .authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(HttpMethod.GET).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+ Scope.READ.getName().toLowerCase()+"')" ))
                    .requestMatchers(HttpMethod.POST).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.WRITE.getName().toLowerCase()+"')"))
                    .requestMatchers(HttpMethod.PUT).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')"))
                    .requestMatchers(HttpMethod.DELETE).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.DELETE.getName().toLowerCase()+"')"))
                    .requestMatchers(HttpMethod.PATCH).access(new WebExpressionAuthorizationManager("#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')"));
        }).authorizeHttpRequests(authorize ->{
                    authorize.anyRequest().authenticated();
        }).exceptionHandling(exception -> {
            exception.authenticationEntryPoint(new AuthenticationEntryPoint() {
                @Override
                public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json");
                    ResultData resultData = ResultData.failed(ResultDataState.NO_PERMISSION.getKey(),"鉴权异常！请重新登陆/联系管理员");
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(yueChipObjectMapper.writeValueAsString(resultData));
                    printWriter.flush();
                    printWriter.close();
                }
            });
        }).oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()));
        })
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
