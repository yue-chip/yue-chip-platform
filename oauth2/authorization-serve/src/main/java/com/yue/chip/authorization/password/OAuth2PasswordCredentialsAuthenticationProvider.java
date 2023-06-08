package com.yue.chip.authorization.password;

import cn.hutool.core.util.ReflectUtil;
import com.yue.chip.core.common.enums.ResultDataState;
import com.yue.chip.security.YueChipUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @date 2023/2/18 下午3:01
 */
public class OAuth2PasswordCredentialsAuthenticationProvider implements AuthenticationProvider {


    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;


    public OAuth2PasswordCredentialsAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2PasswordCredentialsAuthenticationToken authenticationToken = (OAuth2PasswordCredentialsAuthenticationToken)authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        Map<String,String> parameters = authenticationToken.getAdditionalParameters();
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(parameters.get(OAuth2ParameterNames.USERNAME));
        YueChipUserDetails yueChipUserDetails = (YueChipUserDetails) userDetails;
        if (Objects.isNull(yueChipUserDetails)) {
            throw new OAuth2AuthenticationException(new OAuth2Error(ResultDataState.ERROR.getDesc()),"该用户不存在");
        }

        if (!passwordEncoder.matches(parameters.get(OAuth2ParameterNames.PASSWORD),yueChipUserDetails.getPassword())) {
            throw new OAuth2AuthenticationException(new OAuth2Error(ResultDataState.ERROR.getDesc()),"密码错误");
        }
        ReflectUtil.setFieldValue(authenticationToken,"authorities",yueChipUserDetails.getAuthorities());
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(AuthorityUtils.authorityListToSet(yueChipUserDetails.getAuthorities()))
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrant(authenticationToken)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN);

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.build();
//        OAuth2AccessToken accessTokenGenerator = new OAuth2AccessTokenGenerator().generate(tokenContext);
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR);
            throw new OAuth2AuthenticationException(error);
        }


        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient);
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), AuthorityUtils.authorityListToSet(yueChipUserDetails.getAuthorities()));

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR);
                throw new OAuth2AuthenticationException(error);
            }
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

//        // ----- ID token -----
//        OidcIdToken idToken;
//        OAuth2TokenContext idTokenContext = DefaultOAuth2TokenContext.builder()
//                .registeredClient(registeredClient)
////                .principal(new UsernamePasswordAuthenticationToken(username,password))
//                .principal(authenticationToken)
//                .authorizedScopes(AuthorityUtils.authorityListToSet(yueChipUserDetails.getAuthorities()))
//                .tokenType(new OAuth2TokenType(OidcParameterNames.ID_TOKEN))
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                .authorizationGrant(authenticationToken)
//                .build();
//        OAuth2Token generatedIdToken = this.tokenGenerator.generate(idTokenContext);
//        if (!(generatedIdToken instanceof Jwt)) {
//            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR);
//            throw new OAuth2AuthenticationException(error);
//        }
//        idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
//                generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
//        authorizationBuilder.token(idToken, (metadata) ->
//                metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
//        Map<String, Object> add = Collections.emptyMap();
//        if (idToken != null) {
//            add = Maps.newHashMap();
//            add.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
//        }
//        OAuth2Authorization authorization = authorizationBuilder
//                .principalName(parameters.get(OAuth2ParameterNames.USERNAME))
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                .authorizedScopes(registeredClient.getScopes())
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//        this.authorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(registeredClient,clientPrincipal,accessToken,refreshToken,Collections.EMPTY_MAP);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswordCredentialsAuthenticationToken.class.isAssignableFrom(authentication);
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



