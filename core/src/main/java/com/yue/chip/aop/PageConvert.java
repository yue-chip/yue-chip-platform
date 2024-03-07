package com.yue.chip.aop;

import com.yue.chip.core.PageResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.data.domain.Page;

/**
 * @author mr.liu
 * @title: PageConvert
 * @description: 将org.springframework.data.domain.Page转化为com.lion.core.IResultData
 * @date 2020/8/18上午11:38
 */
//@Aspect
//@Component
//@ConditionalOnClass(Page.class)
public class PageConvert {

    @Around(value = "(execution(org.springframework.data.domain.Page com..*.expose..*.*(..)) " +
            "            || execution(org.springframework.data.domain.PageImpl com..*.expose..*.*(..))" +
            "            || execution(org.springframework.data.domain.Page com..*.service..*.*(..))" +
            "            || execution(org.springframework.data.domain.PageImpl com..*.service..*.*(..)) )" +
            "            || execution(org.springframework.data.domain.Page com..*.infrastructure..*.*(..)) )" +
            "            || execution(org.springframework.data.domain.PageImpl com..*.infrastructure..*.*(..)) )" +
            "            || execution(org.springframework.data.domain.Page com..*.application..*.*(..)) )" +
            "            || execution(org.springframework.data.domain.PageImpl com..*.application..*.*(..)) )" +
            "            && ( execution(public * com.yue.chip..*.*(..)) || execution(public * com.xiao.wei..*.*(..)) )")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = pjp.proceed();
        if (obj instanceof Page){
            Page<?> page = (Page<?>) obj;
            PageResultData pageResultData = new PageResultData(page.getContent(),page.getPageable(),page.getTotalElements());
            return pageResultData;
        }
        return obj;
    }
}
