package com.yue.chip.aop.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yue.chip.utils.CurrentUserUtil;
import com.yue.chip.utils.id.SnowflakeUtil;
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

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 系统日志（记录请求数据和返回数据）
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SystemLog {

    private static Logger logger = LoggerFactory.getLogger(SystemLog.class);

    @Around(value = "(@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping) )" +
            "&& execution(public * com.yue.chip..*.*(..)) ")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        LocalDateTime startDateTime = LocalDateTime.now();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        SystemLog systemLog = (SystemLog) method.getAnnotation(com.yue.chip.annotation.SystemLog.class);
//        if ((Objects.nonNull(systemLog) && systemLog.log()) || Objects.isNull(systemLog)){
//            systemLog(method,startDateTime);
//        }
        Object object = pjp.proceed();
//        if ((Objects.nonNull(systemLog) && systemLog.log()) || Objects.isNull(systemLog)){
//            SystemLogData systemLogData = SystemLogDataUtil.get();
//            systemLogData.setResponseData(object);
//            ObjectMapper objectMapper = new ObjectMapper();
//            logger.debug(objectMapper.writeValueAsString(systemLogData));
//        }
        return object;

    }

    private void systemLog(Method method,LocalDateTime startDateTime) throws JsonProcessingException {
        SystemLogData systemLogData = new SystemLogData();
        systemLogData.setTrackId(SnowflakeUtil.getId());
        Map<String,Object> user = CurrentUserUtil.getCurrentUser(false);
        if(Objects.nonNull(user) && user.containsKey("id")){
            systemLogData.setCurrentUserId(Long.valueOf(String.valueOf(user.get("id"))));
        }
        systemLogData.setSequenceNumber(1);
        systemLogData.setTarget(method.toGenericString());
        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime,endDateTime );
        systemLogData.setExecuteTime(duration.toMillis());
        SystemLogDataUtil.set(systemLogData);
    }
}

