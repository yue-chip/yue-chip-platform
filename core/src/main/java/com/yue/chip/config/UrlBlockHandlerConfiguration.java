package com.yue.chip.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.yue.chip.aop.exception.ExceptionData;
import com.yue.chip.core.ResultData;
import com.yue.chip.core.common.enums.ResultDataState;
import com.yue.chip.utils.JsonUtil;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
//
///**
// * @description: sentinel 拒绝访问处理Handler(非网关&url规则定义才生效)
// * @author: Mr.Liu
// * @create: 2020-02-08 11:21
// */
//@Configuration
//@ConditionalOnClass( {UrlBlockHandler.class} )
//public class UrlBlockHandlerConfiguration {
//
//    @PostConstruct
//    public void init(){
//        WebCallbackManager.setUrlBlockHandler(new LionUrlBlockHandler());
//    }
//
//    class LionUrlBlockHandler implements UrlBlockHandler{
//        @Override
//        public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            httpServletResponse.setContentType("application/json;charset=UTF-8");
//            ResultData resultData = ExceptionData.instance(e);
//            resultData.setStatus(ResultDataState.BLOCK_REQUEST.getKey());
//            PrintWriter printWriter = httpServletResponse.getWriter();
//            printWriter.write(JsonUtil.convertToJsonString(resultData));
//            printWriter.flush();
//        }
//    }
//}
