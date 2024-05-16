package com.yue.chip.gateway.loadbalancer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description: 自定义LoadBalancer，为解决开发过程中服务乱窜的问题
 * @author: Mr.Liu
 * @create: 2020-07-10 14:22
 */
public class YueChipLoadBalancer implements ReactorServiceInstanceLoadBalancer{

    private static final Log log = LogFactory.getLog(YueChipLoadBalancer.class);
    private final String serviceId;
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String IS_SHOW_SWAGGER = "isShowSwagger";
    private final String IS_DEVELOPMENT_NODE = "isDevelopmentNode";

    public YueChipLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @SuppressWarnings("rawtypes")
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> {
                            Request<RequestDataContext> requestDataContext = request;
                            HttpHeaders headers = requestDataContext.getContext().getClientRequest().getHeaders();
                            String ip = headers.getFirst("requestHost");
                            if (!StringUtils.hasText(ip)) {
                                ip = headers.getFirst("X-Real-IP");
                            }else if (!StringUtils.hasText(ip)) {
                                ip = headers.getFirst("REMOTE-HOST");
                            }else if (!StringUtils.hasText(ip)) {
                                ip = headers.getFirst("X-Forwarded-For");
                            }
                            String path = requestDataContext.getContext().getClientRequest().getUrl().getPath();
                            return processInstanceResponse(supplier, serviceInstances, ip, path);
                        }
                );
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances, String ip, String path) {
        for (ServiceInstance serviceInstance : serviceInstances){
            Map<String, String> metadata = serviceInstance.getMetadata();
            if (StringUtils.hasText(path) && path.indexOf("v3/api-docs") > -1) {
                if (metadata.containsKey(IS_SHOW_SWAGGER)) {
                    Boolean is_show_swagger = Boolean.valueOf(metadata.get(IS_SHOW_SWAGGER));
                    if (Objects.equals(is_show_swagger,true)) {
                        return new DefaultResponse(serviceInstance);
                    }
                }
            }
            if (StringUtils.hasText(ip)){
                if (Objects.equals(serviceInstance.getHost(),ip) ){
                    return new DefaultResponse(serviceInstance);
                }
            }
        }
        for (ServiceInstance serviceInstance : serviceInstances) {
            Map<String, String> metadata = serviceInstance.getMetadata();
            if (metadata.containsKey(IS_DEVELOPMENT_NODE)) {
                Boolean is_development_node = Boolean.valueOf(metadata.get(IS_DEVELOPMENT_NODE));
                if (Objects.equals(is_development_node, true)) {
                    return new DefaultResponse(serviceInstance);
                }
            }
        }

        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback)supplier).selectedServiceInstance((ServiceInstance)serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        int index = ThreadLocalRandom.current().nextInt(instances.size());

        ServiceInstance instance = instances.get(index);

        return new DefaultResponse(instance);
    }
}
