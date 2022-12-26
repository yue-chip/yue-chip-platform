package com.yue.chip.gateway.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.*;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-19 17:08
 **/
@Component
@Primary
public class GatewaySwaggerResourceProvider implements SwaggerResourcesProvider {

    private final String OAS_20_URL = "/v2/api-docs";
    private final String OAS_30_URL = "/v3/api-docs";

    private final String SWAGGER_ENABLE = "swagger_enable";
    private final String RESOURCE_NAME = "resource_name";

    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private GatewayProperties gatewayProperties;

    @Override
    public List<SwaggerResource> get() {
        List<RouteDefinition> ds = gatewayProperties.getRoutes();
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routeHosts = new ArrayList<>();
        Map<String,String> resourceName = new HashMap<String,String>();
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> Objects.equals(route.getUri().getScheme(), "lb"))
                .subscribe(route -> {
                    Map<String, Object> metadata = route.getMetadata();
                    if (metadata.containsKey(SWAGGER_ENABLE) && Objects.equals(metadata.get(SWAGGER_ENABLE),"true")) {
                        routeHosts.add(route.getUri().getHost());
                        if (metadata.containsKey(RESOURCE_NAME) && Objects.nonNull(metadata.get(RESOURCE_NAME))) {
                            resourceName.put(route.getUri().getHost(), String.valueOf(metadata.get(RESOURCE_NAME)));
                        }
                    }
                });

        Set<String> dealed = new HashSet<>();
        routeHosts.forEach(instance -> {
            String url = "/" + instance.toLowerCase() + OAS_30_URL;
            if (!dealed.contains(url)) {
                dealed.add(url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                if (resourceName.containsKey(instance)){
                    swaggerResource.setName(resourceName.get(instance));
                }else {
                    swaggerResource.setName(instance);
                }
                resources.add(swaggerResource);
            }
        });
        return resources;
    }
}
