package com.yue.chip.authorization.password;

import javax.security.enterprise.identitystore.openid.RefreshToken;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Mr.Liu
 * @date 2023/2/18 下午2:37
 */
public class OAuth2PasswordCredentialsAuthenticationToken extends AbstractAuthenticationToken {

    private final Authentication clientPrincipal;
    @Getter
    private final Map<String, String> additionalParameters;

    private Object credentials;

    private OAuth2AccessToken accessToken;
    private OAuth2RefreshToken refreshToken;


    public OAuth2PasswordCredentialsAuthenticationToken(Authentication clientPrincipal,Map<String, String> additionalParameters) {
        super(Collections.EMPTY_LIST);
        this.clientPrincipal = clientPrincipal;
        this.additionalParameters = additionalParameters;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return clientPrincipal;
    }

    public void setRefreshToken(OAuth2RefreshToken refreshToken){
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(OAuth2AccessToken accessToken){
        this.accessToken = accessToken;
    }

}
