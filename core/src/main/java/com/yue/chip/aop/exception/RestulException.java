package com.yue.chip.aop.exception;

import com.yue.chip.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 统一异常处理
 */

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+1)
public class RestulException {

    private static Logger logger = LoggerFactory.getLogger(RestulException.class);

    @Around(value = "(@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping))" +
            "&& execution(public * com.yue.chip..*.*(..)) ")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object invokeResult = null;
        Object[] args = pjp.getArgs();
        Arrays.stream(args).forEach(arg ->{
            if(arg instanceof BindingResult){
                BindingResult bindingResult = (BindingResult) arg;
                bindingResult.getFieldErrors().forEach(error ->{
                    BusinessException.throwException(error.getDefaultMessage());
                });
            }
        });
        invokeResult = pjp.proceed();
        return invokeResult;
    }
}
