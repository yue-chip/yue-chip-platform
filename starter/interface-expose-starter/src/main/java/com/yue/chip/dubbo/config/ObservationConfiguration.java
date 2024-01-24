package com.yue.chip.dubbo.config;

import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.skywalking.apm.toolkit.micrometer.observation.SkywalkingDefaultTracingHandler;
import org.apache.skywalking.apm.toolkit.micrometer.observation.SkywalkingReceiverTracingHandler;
import org.apache.skywalking.apm.toolkit.micrometer.observation.SkywalkingSenderTracingHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.Liu
 * @date 2023/3/2 下午5:35
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({SkywalkingSenderTracingHandler.class,SkywalkingDefaultTracingHandler.class,SkywalkingReceiverTracingHandler.class})
public class ObservationConfiguration {
    @Bean
    ApplicationModel applicationModel(ObservationRegistry observationRegistry) {
        ApplicationModel applicationModel = ApplicationModel.defaultModel();
        observationRegistry.observationConfig()
                .observationHandler(new ObservationHandler.FirstMatchingCompositeObservationHandler(
                        new SkywalkingSenderTracingHandler(), new SkywalkingReceiverTracingHandler(),
                        new SkywalkingDefaultTracingHandler()
                ));
        applicationModel.getBeanFactory().registerBean(observationRegistry);
        return applicationModel;
    }
}
