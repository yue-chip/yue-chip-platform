package com.yue.chip.authorization.client;

import cn.hutool.core.util.ReflectUtil;
import com.yue.chip.security.YueChipSimpleGrantedAuthority;
import com.yue.chip.security.YueChipUserDetails;
import com.yue.chip.utils.TenantNumberUtil;
import com.yue.chip.utils.YueChipRedisTokenStoreUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.Liu
 * @date 2023/2/18 下午3:01
 */
public class YueChipOAuth2ClientCredentialsAuthenticationProvider implements AuthenticationProvider {


    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;


    public YueChipOAuth2ClientCredentialsAuthenticationProvider( OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof YueChipOAuth2ClientCredentialsAuthenticationToken)) {
            return null;
        }
        YueChipOAuth2ClientCredentialsAuthenticationToken authenticationToken = (YueChipOAuth2ClientCredentialsAuthenticationToken)authentication;
        String grant_type = "";
        Map<String,Object> additionalParameters = authenticationToken.getAdditionalParameters();
        if (additionalParameters.containsKey("grant_type")) {
            grant_type = String.valueOf(additionalParameters.get("grant_type"));
        }
        if (!AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(grant_type)) {
            return null;
        }
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        YueChipSimpleGrantedAuthority grantedAuthority = new YueChipSimpleGrantedAuthority();
        grantedAuthority.setAuthority("client_credentials");
        grantedAuthorityList.add(grantedAuthority);
        YueChipUserDetails userDetails = new YueChipUserDetails(Long.MAX_VALUE,"client_credentials","client_credentials",TenantNumberUtil.getTenantNumber(),grantedAuthorityList);
        ReflectUtil.setFieldValue(authenticationToken,"authorities",userDetails.getAuthorities());
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
//                .authorizedScopes(AuthorityUtils.authorityListToSet(yueChipUserDetails.getAuthorities()))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrant(authenticationToken)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN);
        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR);
            throw new OAuth2AuthenticationException(error);
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), Collections.emptySet());
        Map<String, Object> claims = ((Jwt)generatedAccessToken).getClaims();
        YueChipRedisTokenStoreUtil.store(userDetails,String.valueOf(claims.get("jti")));
        return new OAuth2AccessTokenAuthenticationToken(registeredClient,clientPrincipal,accessToken,null, Collections.emptyMap());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }


}



