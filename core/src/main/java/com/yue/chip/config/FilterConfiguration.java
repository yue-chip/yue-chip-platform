package com.yue.chip.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import java.io.IOException;
//
///**
// * @author mr.liu
// * @Description:
// * @date 2020/8/26下午2:36
// */
//@Configuration
//@ConditionalOnClass({WebMvcAutoConfiguration.class,Filter.class})
//public class FilterConfiguration {
//
//    @Autowired
//    private RequestBodyFilter requestBodyFilter;
//
//    @Bean
//    public FilterRegistrationBean registerAuthFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(requestBodyFilter);
//        registration.addUrlPatterns("/*");
//        registration.setName("requestBodyFilter");
//        registration.setOrder(Integer.MAX_VALUE);
//        return registration;
//    }
//
//
//}
//
//@Component
//@ConditionalOnClass({WebMvcAutoConfiguration.class,Filter.class})
//@Order(Ordered.LOWEST_PRECEDENCE)
//class RequestBodyFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
//        filterChain.doFilter(requestWrapper, servletResponse);
//    }
//}
