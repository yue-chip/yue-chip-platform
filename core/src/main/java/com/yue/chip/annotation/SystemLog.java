package com.yue.chip.annotation;

import java.lang.annotation.*;

/**
 * @author mr.liu
 * @Description:
 * @date 2020/9/17下午1:51
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
    boolean ignore() default false;
}
