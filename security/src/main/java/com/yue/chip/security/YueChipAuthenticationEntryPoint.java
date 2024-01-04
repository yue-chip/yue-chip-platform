package com.yue.chip.security;

import com.yue.chip.core.ResultData;
import com.yue.chip.core.common.enums.ResultDataState;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @author Mr.Liu
 * @date 2023/2/22 下午1:44
 */
public class YueChipAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HttpMessageConverter<Object> responseConverter = new MappingJackson2HttpMessageConverter();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.OK);
        ResultData resultData = ResultData.failed(ResultDataState.LOGIN_FAIL.getKey(),"鉴权失败！请重新登录",authException.getMessage());
        responseConverter.write(resultData, MediaType.APPLICATION_JSON_UTF8,httpResponse);
    }
}
