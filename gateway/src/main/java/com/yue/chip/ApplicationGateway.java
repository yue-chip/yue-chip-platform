package com.yue.chip;

import com.yue.chip.config.EntityAuditorConfiguration;
import com.yue.chip.gateway.loadbalancer.LionLoadBalancerClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication ()
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GatewayReactiveLoadBalancerClientAutoConfiguration.class, EntityAuditorConfiguration.class,  LoadBalancerClientConfiguration.class}))
@EnableDiscoveryClient
@LoadBalancerClients(defaultConfiguration = {LionLoadBalancerClientConfiguration.class})
public class ApplicationGateway {

    public static void main (String[] args) {
        /*
         * new SpringApplicationBuilder(Application.class)
         * .web(WebApplicationType.NONE) .run(args);
         */
        SpringApplication.run(ApplicationGateway.class, args);
    }
}