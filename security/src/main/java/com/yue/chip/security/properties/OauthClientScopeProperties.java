package com.yue.chip.security.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: oauth客户端动态配置权限
 * @author: mr.liu
 * @create: 2020-10-03 14:53
 **/
@Component
@ConditionalOnExpression("!'${security.oauth2.client.scopes}'.isEmpty()")
@ConfigurationProperties(prefix = "security.oauth2.client")
@Data
public class OauthClientScopeProperties {
    private List<Scopes> scopes;
}
