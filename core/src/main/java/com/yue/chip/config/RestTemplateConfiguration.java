package com.yue.chip.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-01-26 14:08
 */
@Configuration
@ConditionalOnClass( {RestTemplate.class} )
@ConditionalOnMissingBean({RestTemplate.class})
public class RestTemplateConfiguration {

    public static final String REST_TAMPLATE_LOAD_BALANCED_BEAN_NAME = "restTemplateLoadBalanced";

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    @Bean(name = REST_TAMPLATE_LOAD_BALANCED_BEAN_NAME)
    @ConditionalOnClass({LoadBalanced.class})
    @LoadBalanced
    public RestTemplate restTemplateLoadBalanced() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            if(!Objects.equals(response.getStatusCode(), HttpStatus.OK)){
                return true;
            }
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
        }

    }
}


