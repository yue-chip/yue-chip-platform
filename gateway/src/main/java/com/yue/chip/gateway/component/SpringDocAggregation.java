package com.yue.chip.gateway.component;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@Slf4j
@Configuration
@EnableScheduling
public class SpringDocAggregation {
    public static final String API_URI = "/%s/v3/api-docs";

    private final String SWAGGER_ENABLE = "swagger_enable";
    private final String RESOURCE_NAME = "resource_name";
    private final SwaggerUiConfigProperties configProperties;

    private RouteLocator routeLocator;

    public SpringDocAggregation(SwaggerUiConfigProperties configProperties, RouteLocator routeLocator) {
        this.configProperties = configProperties;
        this.routeLocator = routeLocator;
    }

    @Scheduled(fixedDelay = 5)
    public void apis() {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        routeLocator.getRoutes()
            .filter(route -> route.getUri().getHost() != null)
            .filter(route -> Objects.equals(route.getUri().getScheme(), "lb"))
            .subscribe(route -> {
                Map<String, Object> metadata = route.getMetadata();
                if (metadata.containsKey(SWAGGER_ENABLE) && Objects.equals(metadata.get(SWAGGER_ENABLE),"true")) {
                    AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
                    swaggerUrl.setName(String.valueOf(metadata.get(RESOURCE_NAME)));
                    swaggerUrl.setUrl(String.format(API_URI, route.getUri().getHost()));
                    urls.add(swaggerUrl);
                }
            });
        configProperties.setUrls(urls);
    }
}
