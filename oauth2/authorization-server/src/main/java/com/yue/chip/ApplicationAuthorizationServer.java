package com.yue.chip;

import com.yue.chip.core.persistence.BaseDaoFactoryBean;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication ( scanBasePackages = {"com.yue.chip.**"} )
@EnableDiscoveryClient
@EnableDubbo
@DubboComponentScan(basePackages = {"com.yue.chip.**"})
@EnableJpaRepositories(basePackages = {"com.yue.chip.dao.**"}, repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
@EntityScan({"com.yue.chip.entity.**"})
@EnableJpaAuditing
public class ApplicationAuthorizationServer {



    public static void main (String[] args) {
        /*
         * new SpringApplicationBuilder(Application.class)
         * .web(WebApplicationType.NONE) .run(args);
         */

        SpringApplication.run(ApplicationAuthorizationServer.class, args);
    }
}