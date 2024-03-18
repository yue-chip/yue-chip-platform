package com.yue.chip.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
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
        messageConverter(restTemplate);
//        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    @Bean(name = REST_TAMPLATE_LOAD_BALANCED_BEAN_NAME)
    @ConditionalOnClass({LoadBalanced.class})
    @LoadBalanced
    public RestTemplate restTemplateLoadBalanced() {
        RestTemplate restTemplate = new RestTemplate();
        messageConverter(restTemplate);
//        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    private void messageConverter(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter : list) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
                break;
            }
        }
    }

    private HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(3000);
        httpRequestFactory.setConnectTimeout(3000);
        return httpRequestFactory;
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


