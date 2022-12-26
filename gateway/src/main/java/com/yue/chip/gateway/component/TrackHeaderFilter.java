package com.yue.chip.gateway.component;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @description: 设置调用链追踪id
 * @author: Mr.Liu
 * @create: 2021-08-03 15:57
 **/
@Component
public class TrackHeaderFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        request = exchange.getRequest().mutate().header("trackId", UUID.randomUUID().toString()).build();
        return chain.filter(exchange);
    }
}
