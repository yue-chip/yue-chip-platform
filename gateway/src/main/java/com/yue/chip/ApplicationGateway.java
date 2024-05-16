package com.yue.chip;

import com.yue.chip.gateway.loadbalancer.YueChipLoadBalancerClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@SpringBootApplication (scanBasePackages = {"com.yue.chip.**"})
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