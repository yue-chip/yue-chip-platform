package com.yue.chip;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication ( scanBasePackages = {"com.yue.chip.**"} )
@EnableDiscoveryClient
@EnableDubbo
@DubboComponentScan(basePackages = {"com.yue.chip.**"})
public class ApplicationAuthorizationServe {

    public static void main (String[] args) {
        /*
         * new SpringApplicationBuilder(Application.class)
         * .web(WebApplicationType.NONE) .run(args);
         */
        SpringApplication.run(ApplicationAuthorizationServe.class, args);
    }
}