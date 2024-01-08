package com.yue.chip.utils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author mr.liu
 * @Description: 数据校验异常工具类
 * @date 2020/8/29下午2:57
 */
public class ValidatorExceptionUtil {

    public static void isViolation(Set<ConstraintViolation<Object>> set){
        if (Objects.isNull(set) || set.size()<=0){
            return;
        }
        set.forEach(cv->{
            Set<ConstraintViolation<Object>> hashSet = new HashSet<ConstraintViolation<Object>>();
            hashSet.add(cv);
            throw new ConstraintViolationException(hashSet);
        });
    }
}
