package com.yue.chip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: 雄大
 * @create: 2021-01-26 17:36
 */
@Configuration
@ConditionalOnWebApplication
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private EnumConverter enumConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumConverter);
    }
}
