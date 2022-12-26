package com.yue.chip.aop.exception;

import com.yue.chip.core.LionObjectMapper;
import com.yue.chip.core.ResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author mr.liu
 * @title: LoadBalancerException
 * @projectName lion
 * @description: RestTemplateLoadBalancer 异常拦截（避免返回一段HTML错误代码）目前只针对BlockingLoadBalancerClient拦截，如有其它负载均衡策略可直接在@Around增加execution
 * @date 2020/7/30下午4:08
 */
@Aspect
@Component
@ConditionalOnClass({BlockingLoadBalancerClient.class})
public class LoadBalancerException {
    private static Logger logger = LoggerFactory.getLogger(LoadBalancerException.class);

    @Around("execution(* org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient.execute(..))")
    public Object around(ProceedingJoinPoint pjp) {
        Object invokeResult = null;
        Object[] args = pjp.getArgs();
        Object controllerClass = pjp.getTarget();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        try {
            invokeResult = pjp.proceed();
        } catch (Throwable e) {
            ResultData resultData = ExceptionData.instance(e);
            ClientHttpResponse clientHttpResponse = new ClientHttpResponse(){
                @Override
                public HttpStatus getStatusCode() throws IOException {
                    return HttpStatus.OK;
                }

                @Override
                public int getRawStatusCode() throws IOException {
                    return 0;
                }

                @Override
                public String getStatusText() throws IOException {
                    return "OK";
                }

                @Override
                public void close() {

                }

                @Override
                public InputStream getBody() throws IOException {
                    LionObjectMapper mapper = new LionObjectMapper();
                    InputStream is =new ByteArrayInputStream(mapper.writeValueAsString(resultData).getBytes("UTF-8"));
                    return is ;
                }

                @Override
                public HttpHeaders getHeaders() {
                    HttpHeaders httpHeaders =new HttpHeaders();
                    httpHeaders.add("source","LoadBalancerException");
                    httpHeaders.add("Content-type","application/json");
                    return httpHeaders;
                }
            };
            return clientHttpResponse;
        }
        return invokeResult;
    }
}
