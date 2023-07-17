package com.yue.chip.api.doc.config;

import jakarta.annotation.Resource;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * swagger 网关api聚合 非通用个性化定制
 */
@Component
public class SpringDocAggregation implements CommandLineRunner {
    public static final String API_URI = "/docs";

    public static final String SWAGGER_ENABLE = "swagger_enable";
    public static final String RESOURCE_NAME = "resource_name";

    @Resource
    private SwaggerUiConfigProperties configProperties;
    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public void run(String... args) throws Exception {
        getDocApi();
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void getDocApi(){
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        List<String> list = discoveryClient.getServices();
        list.forEach(s -> {
            List<ServiceInstance> serviceInstances =discoveryClient.getInstances(s);
            serviceInstances.forEach(instance -> {
                String swagger_enable = instance.getMetadata().get(SWAGGER_ENABLE);
                if (StringUtils.hasText(swagger_enable) && Objects.equals("true",swagger_enable.toLowerCase())) {
                    AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
                    swaggerUrl.setName(String.valueOf(instance.getMetadata().get(RESOURCE_NAME)));
//                    swaggerUrl.setUrl("http://"+ instance.getHost()+":"+instance.getPort()+API_URI);
                    swaggerUrl.setUrl("/"+s+API_URI);
                    urls.add(swaggerUrl);
                }
            });
        });
        configProperties.setUrls(urls);
    }
}
