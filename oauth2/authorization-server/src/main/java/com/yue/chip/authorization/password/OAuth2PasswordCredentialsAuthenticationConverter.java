package com.yue.chip.authorization.password;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Liu
 * @date 2023/2/18 下午2:37
 */
public class OAuth2PasswordCredentialsAuthenticationConverter implements AuthenticationConverter {

    private final String CAPTCHA_CODE_VERIFY = "vCode";
    private final String CAPTCHA_CODE = "code";


//    @Override
//    public Authentication convert(HttpServletRequest request) {
//        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
//        if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
//            return null;
//        }
//        Map<String, String> parameters = getParameters(request);
//        String username = parameters.get(OAuth2ParameterNames.USERNAME);
//        if (!StringUtils.hasText(username)){
//            throw new OAuth2AuthenticationException(new OAuth2Error(ResultDataState.ERROR.getDesc()),"登录账号(username)不能为空");
//        }
//        String password = parameters.get(OAuth2ParameterNames.PASSWORD);
//        if (!StringUtils.hasText(password)){
//            throw new OAuth2AuthenticationException(new OAuth2Error(ResultDataState.ERROR.getDesc()),"密码(password)不能为空");
//        }
//        String vCode = parameters.get(CAPTCHA_CODE_VERIFY);
//        if (!StringUtils.hasText(vCode)){
//            throw new OAuth2AuthenticationException(new OAuth2Error(ResultDataState.ERROR.getDesc()),"验证码校验值(vCode)不能为空");
//        }
//        String code = parameters.get(CAPTCHA_CODE);
//        if (!StringUtils.hasText(code)){
//            throw new OAuth2AuthenticationException(new OAuth2Error(ResultDataState.ERROR.getDesc()),"验证码(code)不能为空");
//        }
//        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
//
//        return new OAuth2PasswordCredentialsAuthenticationToken(clientPrincipal, parameters);
//    }

    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> parameters = new HashMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    parameters.put(key, value);
                }
            }
        });
        return parameters;
    }

    @Override
    public Authentication convert(javax.servlet.http.HttpServletRequest request) {
        return null;
    }
}
