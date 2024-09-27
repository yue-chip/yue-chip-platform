package com.yue.chip.config;

import com.yue.chip.utils.I18nUtils;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.boot.validation.beanvalidation.FilteredMethodValidationPostProcessor;
import org.springframework.boot.validation.beanvalidation.MethodValidationExcludeFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@ConditionalOnClass({ResourceBundleMessageSource.class, ExecutableValidator.class})
@AutoConfigureAfter(MessageSourceAutoConfiguration.class)
@AutoConfigureBefore(ValidationAutoConfiguration.class)
@ConditionalOnWebApplication
public class ValidationConfig {

    @Bean
    @Primary
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Primary
    @ConditionalOnBean(MessageSource.class)
    public LocalValidatorFactoryBean defaultValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
        factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        factoryBean.setValidationMessageSource(messageSource);
        return factoryBean;
    }
    @Bean
    @ConditionalOnBean(MessageSource.class)
    public I18nUtils messageI18nUtil(MessageSource messageSource){
        I18nUtils util = new I18nUtils(messageSource);
        return util;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public MethodValidationPostProcessor methodValidationPostProcessor(Environment environment,
                                                                       @Lazy Validator validator, ObjectProvider<MethodValidationExcludeFilter> excludeFilters) {
        FilteredMethodValidationPostProcessor processor = new FilteredMethodValidationPostProcessor(
                excludeFilters.orderedStream());
        boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
        processor.setProxyTargetClass(proxyTargetClass);
        processor.setValidator(validator);
        return processor;
    }
}
