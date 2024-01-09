package com.yue.chip.aop;

import com.yue.chip.constant.GlobalConstant;
import com.yue.chip.core.YueChipPage;
import com.yue.chip.utils.YueChipPageUtil;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: PageRequestInjection
 * @description: LionPage查询参数注入
 * @date 2020/8/19上午11:01
 */
@Aspect
@Component
@ConditionalOnClass({Servlet.class,Aspect.class})
@Order(Ordered.LOWEST_PRECEDENCE)
public class PageRequestInjection {

    @Around(value = "( @annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping) )" +
            "&& ( execution(public * com.yue.chip..*.*(..)) " +
            "|| execution(public * com.xiao.wei..*.*(..)) )" )
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (int i =0; i< args.length; i++){
            Object arg = args[i];
            if(arg instanceof PageRequest || arg instanceof YueChipPage){
                args[i] = YueChipPageUtil.instance();
            }
        }
        return pjp.proceed(args);
    }


}
