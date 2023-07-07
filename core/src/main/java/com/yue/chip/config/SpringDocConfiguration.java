package com.yue.chip.config;

import com.yue.chip.core.YueChipPage;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.Data;
import org.springdoc.core.utils.SpringDocUtils;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnMissingClass({"org.springframework.cloud.gateway.config.GatewayAutoConfiguration"})
@ConditionalOnWebApplication
@ConditionalOnClass({WebMvcConfigurer.class, PageRequest.class, WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.class, SwaggerWebMvcConfigurer.class})
public class SpringDocConfiguration {
    @Bean
    public OpenAPI springOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        SpringDocUtils.getConfig().replaceWithClass(YueChipPage.class, Page.class);
        return openAPI;
    }

    @Schema
    @Data
    static class Page {
        @Schema(description = "第几页,从0开始计数",example = "1")
        private Integer pageNumber;

        @Schema(description = "每页数据数量",example = "10")
        private Integer pageSize;
    }
}

