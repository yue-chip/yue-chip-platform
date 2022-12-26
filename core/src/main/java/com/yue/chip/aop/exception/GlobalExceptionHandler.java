package com.yue.chip.aop.exception;

import com.yue.chip.core.IResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 全局异常处理
 * @author: Mr.Liu
 * @create: 2020-02-12 22:22
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public IResultData exception(Throwable e) {
        return ExceptionData.instance(e);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public IResultData exception(MethodArgumentNotValidException e) {
//        return ExceptionData.instance(e);
//    }
}
