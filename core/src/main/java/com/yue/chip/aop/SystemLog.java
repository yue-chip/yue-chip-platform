package com.yue.chip.aop;

import com.yue.chip.core.SystemLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.Servlet;
import java.lang.reflect.Method;

/**
 * @author coby
 * @description: TODO
 * @date 2024/1/8 下午2:18
 */
@Aspect
@Component
@ConditionalOnClass({Servlet.class,Aspect.class})
@Order(Ordered.LOWEST_PRECEDENCE)
public class SystemLog {

    @Resource
    private SystemLogService systemLogService;


    @Around(value = "( @annotation(com.yue.chip.annotation.SystemLog) && ( execution(public * com.yue.chip..*.*(..)) || execution(public * com.xiao.wei..*.*(..)) ))" )
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        Method method = joinPointObject.getMethod();
        com.yue.chip.annotation.SystemLog systemLog = method.getAnnotation(com.yue.chip.annotation.SystemLog.class);
        Object[] args = pjp.getArgs();
        Object object = pjp.proceed(args);
        systemLogService.save(systemLog.value());
        return object;
    }
}
