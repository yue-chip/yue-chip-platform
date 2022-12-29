package com.yue.chip.resource.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 排除鉴权url
 * @author: Mr.Liu
 * @create: 2020-02-06 20:47
 */
@Component
@ConditionalOnExpression("!'${security.oauth2.ignore-url}'.isEmpty()")
@ConfigurationProperties(prefix = "security.oauth2")
@Data
public class AuthorizationIgnoreProperties  {

    private List<String> ignoreUrl = new ArrayList<>();

}
