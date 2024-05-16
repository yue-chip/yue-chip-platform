package com.yue.chip.gateway.loadbalancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.loadbalancer.support.LoadBalancerEnvironmentPropertyUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.RetrySpec;

import java.util.Objects;

/**
 * @description: 自定义LoadBalancer，为解决开发过程中服务乱窜的问题
 * @author: Mr.Liu
 * @create: 2020-07-10 16:46
 */

//public class YueChipLoadBalancerClientConfiguration  LoadBalancerClientConfiguration {
public class YueChipLoadBalancerClientConfiguration {

    private static final int REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER = 193827465;

    @Value("${spring.cloud.gateway.development.mode.enabled:false}")
    private Boolean mode;

    @Bean
    @ConditionalOnMissingBean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return Objects.equals(mode,true) ?
                new YueChipLoadBalancer(loadBalancerClientFactory.getLazyProvider(name,ServiceInstanceListSupplier.class), name) :
                new RandomLoadBalancer(loadBalancerClientFactory.getLazyProvider(name,ServiceInstanceListSupplier.class), name);
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnReactiveDiscoveryEnabled
    @Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER)
    public static class ReactiveSupportConfiguration {

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.DefaultConfigurationCondition.class)
        public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient().withCaching().build(context);
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.ZonePreferenceConfigurationCondition.class)
        public ServiceInstanceListSupplier zonePreferenceDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient().withCaching().withZonePreference()
                    .build(context);
        }

        @Bean
        @ConditionalOnBean(LoadBalancerClientFactory.class)
        @ConditionalOnMissingBean
        public XForwardedHeadersTransformer xForwarderHeadersTransformer(LoadBalancerClientFactory clientFactory) {
            return new XForwardedHeadersTransformer(clientFactory);
        }

        @Bean
        @ConditionalOnBean({ ReactiveDiscoveryClient.class, WebClient.Builder.class })
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.HealthCheckConfigurationCondition.class)
        public ServiceInstanceListSupplier healthCheckDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient().withHealthChecks().build(context);
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.RequestBasedStickySessionConfigurationCondition.class)
        public ServiceInstanceListSupplier requestBasedStickySessionDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient().withCaching()
                    .withRequestBasedStickySession().build(context);
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.SameInstancePreferenceConfigurationCondition.class)
        public ServiceInstanceListSupplier sameInstancePreferenceServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient().withCaching()
                    .withSameInstancePreference().build(context);
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.WeightedConfigurationCondition.class)
        public ServiceInstanceListSupplier weightedServiceInstanceListSupplier(ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient().withCaching().withWeighted()
                    .build(context);
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.SubsetConfigurationCondition.class)
        public ServiceInstanceListSupplier subsetServiceInstanceListSupplier(ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient().withSubset().withCaching()
                    .build(context);
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBlockingDiscoveryEnabled
    @Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER + 1)
    public static class BlockingSupportConfiguration {

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.DefaultConfigurationCondition.class)
        public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withCaching().build(context);
        }

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.ZonePreferenceConfigurationCondition.class)
        public ServiceInstanceListSupplier zonePreferenceDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withCaching()
                    .withZonePreference().build(context);
        }

        @Bean
        @ConditionalOnBean({ DiscoveryClient.class, RestTemplate.class })
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.HealthCheckConfigurationCondition.class)
        public ServiceInstanceListSupplier healthCheckDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withBlockingHealthChecks()
                    .build(context);
        }

        @Bean
        @ConditionalOnBean({ DiscoveryClient.class, RestClient.class })
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.HealthCheckConfigurationCondition.class)
        public ServiceInstanceListSupplier healthCheckRestClientDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient()
                    .withBlockingRestClientHealthChecks().build(context);
        }

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.RequestBasedStickySessionConfigurationCondition.class)
        public ServiceInstanceListSupplier requestBasedStickySessionDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withCaching()
                    .withRequestBasedStickySession().build(context);
        }

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.SameInstancePreferenceConfigurationCondition.class)
        public ServiceInstanceListSupplier sameInstancePreferenceServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withCaching()
                    .withSameInstancePreference().build(context);
        }

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.WeightedConfigurationCondition.class)
        public ServiceInstanceListSupplier weightedServiceInstanceListSupplier(ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withCaching().withWeighted()
                    .build(context);
        }

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @Conditional(YueChipLoadBalancerClientConfiguration.SubsetConfigurationCondition.class)
        public ServiceInstanceListSupplier subsetServiceInstanceListSupplier(ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withSubset().withCaching()
                    .build(context);
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBlockingDiscoveryEnabled
    @ConditionalOnClass(RetryTemplate.class)
    @Conditional(YueChipLoadBalancerClientConfiguration.BlockingOnAvoidPreviousInstanceAndRetryEnabledCondition.class)
    @AutoConfigureAfter(LoadBalancerClientConfiguration.BlockingSupportConfiguration.class)
    @ConditionalOnBean(ServiceInstanceListSupplier.class)
    public static class BlockingRetryConfiguration {

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @Primary
        public ServiceInstanceListSupplier retryAwareDiscoveryClientServiceInstanceListSupplier(
                ServiceInstanceListSupplier delegate) {
            return new RetryAwareServiceInstanceListSupplier(delegate);
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnReactiveDiscoveryEnabled
    @Conditional(YueChipLoadBalancerClientConfiguration.ReactiveOnAvoidPreviousInstanceAndRetryEnabledCondition.class)
    @AutoConfigureAfter(LoadBalancerClientConfiguration.ReactiveSupportConfiguration.class)
    @ConditionalOnBean(ServiceInstanceListSupplier.class)
    @ConditionalOnClass(RetrySpec.class)
    public static class ReactiveRetryConfiguration {

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @Primary
        public ServiceInstanceListSupplier retryAwareDiscoveryClientServiceInstanceListSupplier(
                ServiceInstanceListSupplier delegate) {
            return new RetryAwareServiceInstanceListSupplier(delegate);
        }

    }

    static final class BlockingOnAvoidPreviousInstanceAndRetryEnabledCondition extends AllNestedConditions {

        private BlockingOnAvoidPreviousInstanceAndRetryEnabledCondition() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(value = "spring.cloud.loadbalancer.retry.enabled", havingValue = "true",
                matchIfMissing = true)
        static class LoadBalancerRetryEnabled {

        }

        @Conditional(YueChipLoadBalancerClientConfiguration.AvoidPreviousInstanceEnabledCondition.class)
        static class AvoidPreviousInstanceEnabled {

        }

    }

    static final class ReactiveOnAvoidPreviousInstanceAndRetryEnabledCondition extends AllNestedConditions {

        private ReactiveOnAvoidPreviousInstanceAndRetryEnabledCondition() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(value = "spring.cloud.loadbalancer.retry.enabled", havingValue = "true")
        static class LoadBalancerRetryEnabled {

        }

        @Conditional(YueChipLoadBalancerClientConfiguration.AvoidPreviousInstanceEnabledCondition.class)
        static class AvoidPreviousInstanceEnabled {

        }

    }

    static class AvoidPreviousInstanceEnabledCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return LoadBalancerEnvironmentPropertyUtils.trueOrMissingForClientOrDefault(context.getEnvironment(),
                    "retry.avoid-previous-instance");
        }

    }

    static class DefaultConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return LoadBalancerEnvironmentPropertyUtils.equalToOrMissingForClientOrDefault(context.getEnvironment(),
                    "configurations", "default");
        }

    }

    static class ZonePreferenceConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
                    "configurations", "zone-preference");
        }

    }

    static class HealthCheckConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
                    "configurations", "health-check");
        }

    }

    static class RequestBasedStickySessionConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
                    "configurations", "request-based-sticky-session");
        }

    }

    static class SameInstancePreferenceConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
                    "configurations", "same-instance-preference");
        }

    }

    static class WeightedConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
                    "configurations", "weighted");
        }

    }

    static class SubsetConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
                    "configurations", "subset");
        }

    }
}
