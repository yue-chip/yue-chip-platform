package com.yue.chip;

//import com.yue.chip.gateway.loadbalancer.YueChipLoadBalancerClientConfiguration;
import com.yue.chip.gateway.loadbalancer.YueChipLoadBalancerClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication (scanBasePackages = {"com.yue.chip.**"})
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GatewayReactiveLoadBalancerClientAutoConfiguration.class, LoadBalancerClientConfiguration.class}))
@EnableDiscoveryClient
@LoadBalancerClients(defaultConfiguration = {YueChipLoadBalancerClientConfiguration.class})
public class ApplicationGateway {


    public static void main (String[] args) {
        /*
         * new SpringApplicationBuilder(Application.class)
         * .web(WebApplicationType.NONE) .run(args);
         */
        SpringApplication.run(ApplicationGateway.class, args);
    }

}