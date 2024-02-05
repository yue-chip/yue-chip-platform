package com.yue.chip.authorization.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken;

import java.util.Map;
import java.util.Set;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 上午11:46
 */
public class YueChipOAuth2ClientCredentialsAuthenticationToken extends OAuth2ClientCredentialsAuthenticationToken {
    /**
     * Constructs an {@code OAuth2ClientCredentialsAuthenticationToken} using the provided parameters.
     *
     * @param clientPrincipal      the authenticated client principal
     * @param scopes               the requested scope(s)
     * @param additionalParameters the additional parameters
     */
    public YueChipOAuth2ClientCredentialsAuthenticationToken(Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(clientPrincipal, scopes, additionalParameters);

    }
}
