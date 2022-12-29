package com.yue.chip.config;

import com.fasterxml.classmate.TypeResolver;
import com.yue.chip.core.YueChipPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.SpringfoxWebConfiguration;
import springfox.documentation.spring.web.paths.DefaultPathProvider;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.configuration.Swagger2WebMvcConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-06 10:39
 **/
@Configuration
@ConditionalOnClass({Swagger2WebMvcConfiguration.class})
@ConditionalOnMissingClass({"org.springframework.cloud.gateway.config.GatewayAutoConfiguration"})
@ConditionalOnWebApplication
@EnableOpenApi
@AutoConfigureBefore({SpringfoxWebConfiguration.class})
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    private String basePath;

    @Bean
    public Docket docket(ApplicationContext applicationContext){
        List<Response> responses = new ArrayList<Response>();
//        Response response = new Response("200","",true, Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST);
        Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.POST,responses)
                .globalResponses(HttpMethod.GET,responses)
                .globalResponses(HttpMethod.DELETE,responses)
                .globalResponses(HttpMethod.PUT,responses)
                .globalResponses(HttpMethod.PATCH,responses)
                .globalResponses(HttpMethod.OPTIONS,responses)
                .select()
                //设置basePackage会将包下的所有被@Api标记类的所有方法作为api
//                .apis(RequestHandlerSelectors.basePackage("com.lion"))
//                .apis(RequestHandlerSelectors.basePackage("com.smartlinks"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
        removeDefaultPlugin(applicationContext);
        return docket;
    }

    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.LOWEST_PRECEDENCE;
            }
            @Override
            public List<AlternateTypeRule> rules() {
                List<AlternateTypeRule> list = new ArrayList<AlternateTypeRule>();
                list.add(newRule(resolver.resolve(YueChipPage.class), resolver.resolve(Page.class)));
                return list;
            }
        };
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().build();
    }

    private void removeDefaultPlugin(ApplicationContext applicationContext) {
//        PluginRegistry<OperationBuilderPlugin, DocumentationType> pluginRegistry = applicationContext.getBean("operationBuilderPluginRegistry", PluginRegistry.class);
//        List<OperationBuilderPlugin> plugins = pluginRegistry.getPlugins();
//        OpenApiResponseReader openApiResponseReader = applicationContext.getBean(OpenApiResponseReader.class);
//        if (pluginRegistry.contains(openApiResponseReader)) {
//            List<OperationBuilderPlugin> plugins_new = new ArrayList<OperationBuilderPlugin>(plugins);
//            plugins_new.remove(openApiResponseReader);
//            try {
//                Field field = PluginRegistrySupport.class.getDeclaredField("plugins");
//                field.setAccessible(true);
//                field.set(pluginRegistry, plugins_new);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }


    @Schema
    @Data
    static class Page {
        @Schema(description = "第几页,从0开始计数",example = "1")
        private Integer pageNumber;

        @Schema(description = "每页数据数量",example = "10")
        private Integer pageSize;
    }

    @Bean
    @Primary
    public PathProvider pathProvider() {
        return new DefaultPathProvider() {
            @Override
            public String getOperationPath(String operationPath) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/"+basePath+"/");
                return Paths.removeAdjacentForwardSlashes(uriComponentsBuilder.path(operationPath).build().toString());
            }
        };
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
