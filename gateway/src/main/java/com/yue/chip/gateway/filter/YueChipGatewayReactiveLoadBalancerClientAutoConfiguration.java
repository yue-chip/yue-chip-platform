package com.yue.chip.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerServiceInstanceCookieFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @description: 自定义LoadBalancer 用于解决开发环境服务乱传问题
 * @author: mr.liu
 * @create: 2020-10-09 19:39
 **/
@Configuration(proxyBeanMethods = false)
public class YueChipGatewayReactiveLoadBalancerClientAutoConfiguration {

    @Value("${spring.cloud.gateway.development.mode.enabled:false}")
    private Boolean mode;

    @Bean
    public ReactiveLoadBalancerClientFilter gatewayLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory,
                                                                            GatewayLoadBalancerProperties properties, LoadBalancerProperties loadBalancerProperties) {
        return Objects.equals(mode,true) ?
                new YueChipReactiveLoadBalancerClientFilter(clientFactory,properties) :
                new ReactiveLoadBalancerClientFilter(clientFactory,properties);
    }


    @Bean
    public LoadBalancerServiceInstanceCookieFilter loadBalancerServiceInstanceCookieFilter(
            ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerProperties) {
        return new LoadBalancerServiceInstanceCookieFilter(loadBalancerProperties);
    }


}
