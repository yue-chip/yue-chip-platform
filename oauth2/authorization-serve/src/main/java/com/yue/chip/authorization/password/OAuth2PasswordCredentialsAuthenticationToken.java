package com.yue.chip.authorization.password;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * @author Mr.Liu
 * @date 2023/2/18 下午2:37
 */
public class OAuth2PasswordCredentialsAuthenticationToken extends AbstractAuthenticationToken {

    private final Authentication clientPrincipal;
    @Getter
    private final Map<String, String> additionalParameters;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public OAuth2PasswordCredentialsAuthenticationToken(Authentication clientPrincipal,Map<String, String> additionalParameters,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
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
}
