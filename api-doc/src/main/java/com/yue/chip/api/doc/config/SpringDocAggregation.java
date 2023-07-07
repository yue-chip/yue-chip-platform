package com.yue.chip.api.doc.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SpringDocAggregation implements CommandLineRunner {
    public static final String API_URI = "/%s/v3/api-docs";

    private final String SWAGGER_ENABLE = "swagger_enable";
    private final String RESOURCE_NAME = "resource_name";

//    @NacosInjected
//    NamingService namingService;

    @Override
    public void run(String... args) throws Exception {
//        List<Instance> allInstances = namingService.getAllInstances("");
//        allInstances.forEach(all->{
//            System.err.println(all.getIp());    //ip
//            System.err.println(all.getPort());  //端口
//        });
    }


//    private final SwaggerUiConfigProperties configProperties;
//
//    private RouteLocator routeLocator;
//
//    public SpringDocAggregation(SwaggerUiConfigProperties configProperties, RouteLocator routeLocator) {
//        this.configProperties = configProperties;
//        this.routeLocator = routeLocator;
//    }
//
//    @Scheduled(fixedDelay = 5)
//    public void apis() {
//        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
//        routeLocator.getRoutes()
//            .filter(route -> route.getUri().getHost() != null)
//            .filter(route -> Objects.equals(route.getUri().getScheme(), "lb"))
//            .subscribe(route -> {
//                Map<String, Object> metadata = route.getMetadata();
//                if (metadata.containsKey(SWAGGER_ENABLE) && Objects.equals(metadata.get(SWAGGER_ENABLE),"true")) {
//                    AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
//                    swaggerUrl.setName(String.valueOf(metadata.get(RESOURCE_NAME)));
//                    swaggerUrl.setUrl(String.format(API_URI, route.getUri().getHost()));
//                    urls.add(swaggerUrl);
//                }
//            });
//        configProperties.setUrls(urls);
//    }
}
