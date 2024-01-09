package com.yue.chip.annotation;

import java.lang.annotation.*;

/**
 * 排除授权认证
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    String value() default "";
}
