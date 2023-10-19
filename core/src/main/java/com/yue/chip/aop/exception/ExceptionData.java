package com.yue.chip.aop.exception;

import com.yue.chip.core.ResultData;
import com.yue.chip.core.common.enums.ResultDataState;
import com.yue.chip.exception.AuthorizationException;
import com.yue.chip.exception.BusinessException;
import jakarta.persistence.Id;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * @description: 统一异常处理数据返回
 * @author: Mr.Liu
 * @create: 2020-02-07 17:08
 */
public class ExceptionData {

    public static ResultData instance(final Throwable e){
        e.printStackTrace();
        ResultData resultData = ResultData.builder()
                                .message(e.getMessage())
                                .status(ResultDataState.ERROR.getKey())
                                .build();
        try {
            handle(e, resultData);
        } catch (Throwable ex) {
            resultData.setMessage("程序开小差了！请与管理员联系！");
            ex.printStackTrace();
        }
        return resultData;
    }

    private static void handle(final Throwable e,final ResultData resultData) throws Throwable{
        if (e instanceof InternalAuthenticationServiceException){
            resultData.setMessage( "用户名/密码错误");
        }else if (e instanceof IllegalArgumentException){
            resultData.setMessage( e.getMessage());
        }else if (e instanceof HttpMessageNotReadableException){
            resultData.setMessage( "数据格式错误(请出入正确的json数据)");
        }else if (e instanceof BusinessException){
            resultData.setMessage(e.getMessage());
        }else if (e instanceof AccessDeniedException ){
            resultData.setMessage( "权限不足，不允许访问/登陆异常，请重新登陆");
            resultData.setStatus(ResultDataState.NO_PERMISSION.getKey());
        }else if (e instanceof ConstraintViolationException){
            Set<ConstraintViolation<?>> set = ((ConstraintViolationException) e).getConstraintViolations();
            if (Objects.nonNull(set)) {
                Iterator<ConstraintViolation<?>> iterator = set.iterator();
                if (iterator.hasNext()){
                    resultData.setMessage(iterator.next().getMessage());
                }
            }
        }else if (e instanceof BindException){
            BindException bindException = (BindException)e;
            analysisBindingResult(bindException.getBindingResult(),resultData);
        }else if (e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException)e;
            analysisBindingResult(methodArgumentNotValidException.getBindingResult(),resultData);
        }else if (e instanceof AuthorizationException || e instanceof InsufficientAuthenticationException){
//            if (e instanceof OAuth2Exception){
//                OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
//                if (oAuth2Exception.getHttpErrorCode()==403) {
//                    resultData.setMessage("权限不足：" + e.getMessage());
//                    resultData.setStatus(ResultDataState.ERROR.getKey());
//
//                }else if (oAuth2Exception.getHttpErrorCode()==401||oAuth2Exception.getHttpErrorCode()==400){
//                    resultData.setMessage("登陆异常，请重新登陆");
//                    resultData.setStatus(ResultDataState.LOGIN_FAIL.getKey());
//                }
//            }else
            if (e instanceof InsufficientAuthenticationException || e instanceof AuthorizationException){
                resultData.setMessage("登陆异常，请重新登陆");
                resultData.setStatus(ResultDataState.LOGIN_FAIL.getKey());
            }
        }else if (e instanceof ObjectOptimisticLockingFailureException){
            resultData.setMessage( "该数据发生变化，请重新获取新数据！");
        }else if (e instanceof CannotCreateTransactionException) {
            resultData.setMessage( "获取租户信息/切换租户数据库失败");
        }
//        else if (e instanceof BlockException || e.getCause() instanceof BlockException) {
//            resultData.setMessage("sentinel block request(可能触发熔断/降级/限流……保护)");
//        }
        else {
            resultData.setMessage("程序开小差了！请与管理员联系！");
        }
        resultData.setExceptionMessage(e.getMessage());
    }

    private static void analysisBindingResult(final BindingResult bindingResult, final ResultData resultData){
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            resultData.setMessage(fieldError.getDefaultMessage());
            break;
        }
    }


}
