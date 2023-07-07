package com.yue.chip.api.doc;

import com.yue.chip.api.doc.config.SpringDocAggregation;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @date 2023/7/7 下午1:47
 */
@RestController
public class SpringDocController {

    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/{instanceName}/docs")
    public String doc(@PathVariable String instanceName){
        List<ServiceInstance> serviceInstances =discoveryClient.getInstances(instanceName);
        for (ServiceInstance serviceInstance : serviceInstances) {
            String swagger_enable = serviceInstance.getMetadata().get(SpringDocAggregation.SWAGGER_ENABLE);
            if (StringUtils.hasText(swagger_enable) && Objects.equals("true", swagger_enable.toLowerCase())) {
                String response = restTemplate.getForObject("http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/v3/api-docs", String.class);
                return response;
            }
        }
        return "";
    }
}
